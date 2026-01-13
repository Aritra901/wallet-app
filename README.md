# Wallet Service Microservice

Spring Boot 4 application for managing digital wallets in a payments system. Supports user registration, wallet creation, deposits, withdrawals, and peer-to-peer transfers with atomic balance updates and transaction logging.

## Overview

This repository provides a project skeleton for a RESTful microservice using Java 17, Gradle Kotlin DSL, Spring Data JPA, and PostgreSQL. The initial implementation covers user registration and wallet creation with full test coverage. Extend it to implement additional features per [INSTRUCTIONS.md](INSTRUCTIONS.md).

Distributed as a ZIP archive including Git history for tracking development progress.

Group: `com.rs.payments`. Version: `0.0.1-SNAPSHOT`. Description: `wallet-service`.

## Prerequisites

- Java 17 (JDK, enforced via toolchain)
- Gradle 8.x (via wrapper)
- Docker and Docker Compose (for local database and Testcontainers)
- IntelliJ IDEA or compatible IDE (recommended for Spring Boot and Gradle support)
- Git (for history inspection)

## Local Setup

1. Extract the ZIP archive to a directory: `unzip wallet-service.zip` (or use GUI tool).
2. Navigate to the project root: `cd wallet-service`.
3. Inspect Git history: `git log --oneline` (demonstrates initial commits).
4. Import into IDE: Open as Gradle project in IntelliJ (File > Open > select `build.gradle.kts`).

## Running the Application

### Full Stack (Docker Compose)

Starts PostgreSQL and the application container. (Assumes `docker-compose.yml` presence; configure as needed.)

```bash
docker-compose up --build
```

- Application: http://localhost:8080
- Database: localhost:5432 (user: `user`, password: `password`, db: `walletdb`)
- Logs: Monitor via `docker-compose logs -f`

Stop with `docker-compose down`.

### Development Mode (IDE/Gradle)

1. Start PostgreSQL: `docker-compose up -d db`.
2. Run application: `./gradlew bootRun` (or IDE run configuration for main class).

Configuration uses `application.yml` for local datasource (update URL if needed).

## API Documentation

- OpenAPI/Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Endpoints (initial):
- `POST /users`: Create user.
- `POST /wallets`: Create wallet for user.

## Code Quality Checks

Run static analysis tools via Gradle tasks.

- Checkstyle (main sources): `./gradlew checkstyleMain`
- PMD (main sources, custom ruleset): `./gradlew pmdMain`
- SpotBugs (main sources, HTML/XML reports in `build/reports/spotbugs`): `./gradlew spotbugsMain`
- All checks: `./gradlew check` (includes unit/integration tests)

Failures are ignored by default; review reports for issues.

## Running Tests

Uses JUnit 5, Mockito, and Testcontainers for PostgreSQL. Tests in `src/test/java`; integration tests in `src/integration/java` (tagged `@Tag("integration")`).

- Unit tests: `./gradlew test`
- Integration tests: `./gradlew integrationTest`
- All tests: `./gradlew check`
- Coverage report (JaCoCo, HTML in `build/reports/jacoco/test/html/index.html`, XML in `build/reports/jacoco/test/jacocoTestReport.xml`): `./gradlew jacocoTestReport` (runs after tests)

Aim for 100% coverage on new code.

## Building the Project

```bash
./gradlew build
```

Produces executable JAR: `build/libs/wallet-service-0.0.1-SNAPSHOT.jar`.

Run JAR: `java -jar build/libs/wallet-service-0.0.1-SNAPSHOT.jar`.

Clean build: `./gradlew clean build`.

## Database Schema

Hibernate DDL auto: `update` (dev only). Entities (using UUID for primary keys):
- `users`: id (UUID), username, email (unique).
- `wallets`: id (UUID), balance (one-to-one with user).
- `transactions`: id (UUID), amount, type, timestamp.

## Further Development

Implement user stories in [INSTRUCTIONS.md](INSTRUCTIONS.md) using TDD. 
Commit incrementally to the main branch with descriptive messages (e.g., "Add failing test for deposit endpoint").

For CI: `.github/workflows/ci.yml` runs on push/PR (adapt as needed).

## Submission

To prepare the project for submission, run:

```bash
./gradlew prepareSubmission
```

This creates a ZIP file: `submission-<your-username>-<date>.zip`.

**Important**: 
- If the file name ends with `-anonymous`, the submission **will not be accepted**.
- Ensure your git author name is set correctly:
  ```bash
  git config user.name "Your Name"
  git config user.email "your.email@example.com"
  ```
- Commit your changes before running the task.

## Troubleshooting

- **Port conflict**: Change `server.port` in `application.yml`.
- **Database connection**: Verify Docker container health (`docker ps`); check logs.
- **Gradle issues**: `./gradlew clean` and retry; ensure Java 17 via `java -version`.
- **Tests fail**: Ensure Docker for Testcontainers; use `create-drop` mode in tests.
- **Lombok issues**: Enable annotation processing in IDE.


For questions, refer to [INSTRUCTIONS.md](INSTRUCTIONS.md).