package com.rs.payments.wallet.model;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Should create user with valid data")
    void shouldCreateUserWithValidData() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "testuser", "test@example.com", null);

        assertEquals(id, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertNull(user.getWallet());
    }

    @Test
    @DisplayName("Should update user fields")
    void shouldUpdateUserFields() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setUsername("updatedUser");
        user.setEmail("updated@example.com");

        assertEquals(id, user.getId());
        assertEquals("updatedUser", user.getUsername());
        assertEquals("updated@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = new User(id1, "user1", "user1@example.com", null);
        User user2 = new User(id1, "user1", "user1@example.com", null);
        User user3 = new User(id2, "user2", "user2@example.com", null);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}
