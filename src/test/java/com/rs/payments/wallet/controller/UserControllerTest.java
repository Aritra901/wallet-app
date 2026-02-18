package com.rs.payments.wallet.controller;

import java.util.UUID;
import com.rs.payments.wallet.dto.CreateUserRequest;
import com.rs.payments.wallet.model.User;
import com.rs.payments.wallet.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        // Given
        CreateUserRequest request = new CreateUserRequest("testuser", "test@example.com");

        User createdUser = new User(UUID.randomUUID(), "testuser", "test@example.com", null);
        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        // When
        ResponseEntity<?> response = userController.createUser(request);

        // Then
        assertEquals(201, response.getStatusCode().value());
        assertEquals(createdUser, response.getBody());
        verify(userService, times(1)).createUser(any(User.class));
    }
}
