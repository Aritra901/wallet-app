import org.gradle.kotlin.dsl.register
import org.gradle.api.plugins.quality.PmdExtension
import java.text.SimpleDateFormat
import java.util.Date
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

plugins {
    java
    jacoco
    checkstyle
    pmd
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.github.spotbugs") version "6.0.26"
}

checkstyle {
    toolVersion = "10.21.1"
    isIgnoreFailures = true
    maxWarnings = 0
}

spotbugs {
    toolVersion = "4.9.0"
    ignoreFailures = true
    showProgress = true
}

configure<PmdExtension> {
    toolVersion = "7.9.0"
    isIgnoreFailures = true
    ruleSetFiles = files("config/pmd/ruleset.xml")
    ruleSets = listOf()
}

group = "com.rs.payments"
version = "0.0.1-SNAPSHOT"
description = "wallet-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

sourceSets {
    test {
        java.srcDir("src/integration/java")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("org.postgresql:postgresql")
    
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.3")
}

val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform {
        includeTags("integration")
    }
    shouldRunAfter("test")
    outputs.upToDateWhen { false }
}

tasks.check {
    dependsOn(integrationTest)
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    reports {
        register("html") {
            enabled = true
        }
        register("xml") {
            enabled = true
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test, integrationTest)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.register("prepareSubmission") {
    group = "distribution"
    description = "Prepares the project for submission by archiving git history."

    doLast {
        val gitAuthor = try {
            ProcessBuilder("git", "log", "-1", "--format=%an")
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .start()
                .inputStream
                .bufferedReader()
                .readText()
                .trim()
        } catch (e: Exception) {
            ""
        }

        val username = if (gitAuthor.isEmpty()) {
            "anonymous"
        } else {
            gitAuthor.replace(Regex("[^a-zA-Z0-9]"), "")
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = dateFormat.format(Date())
        val fileName = "submission-$username-$date.zip"

        val projectDir = layout.projectDirectory.asFile
        val zipFile = File(projectDir, fileName)

        println("Zipping project to ${zipFile.absolutePath}")

        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            projectDir.walk().onEnter { dir ->
                val name = dir.name
                // Exclude the build directory, .gradle, and the zip file itself to prevent recursion loop
                name != "build" && name != ".gradle" && name != ".idea"
            }.forEach { file ->
                if (file.isFile && file.name != fileName) {
                    val relativePath = file.relativeTo(projectDir).path
                    val entry = ZipEntry(relativePath)
                    zos.putNextEntry(entry)
                    file.inputStream().use { input ->
                        input.copyTo(zos)
                    }
                    zos.closeEntry()
                }
            }
        }
        println("Zip created successfully at: ${zipFile.absolutePath}")
    }
}

