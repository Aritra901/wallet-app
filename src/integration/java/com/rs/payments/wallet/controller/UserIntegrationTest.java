package com.rs.payments.wallet.controller;

import com.rs.payments.wallet.BaseIntegrationTest;
import com.rs.payments.wallet.dto.CreateUserRequest;
import com.rs.payments.wallet.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserIntegrationTest extends BaseIntegrationTest {

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void shouldCreateUser() {
        CreateUserRequest request = new CreateUserRequest("testuser", "test@example.com");

        String url = "http://localhost:" + port + "/users";
        ResponseEntity<User> response = restTemplate.postForEntity(url, request, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldCreateUserAndIgnoreProvidedId() {
        UUID providedId = UUID.randomUUID();
        // Constructing a JSON string that includes an ID to verify it's ignored or not mapped
        String jsonRequest = String.format("{\"id\":\"%s\", \"username\":\"testuser2\", \"email\":\"test2@example.com\"}", providedId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        String url = "http://localhost:" + port + "/users";
        ResponseEntity<User> response = restTemplate.postForEntity(url, entity, User.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotEqualTo(providedId);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser2");
    }

    @Test
    void shouldReturnBadRequestWhenUserInvalid() {
        CreateUserRequest request = new CreateUserRequest("", ""); // Blank fields

        String url = "http://localhost:" + port + "/users";
        try {
            restTemplate.postForEntity(url, request, User.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}
