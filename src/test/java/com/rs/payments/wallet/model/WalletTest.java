package com.rs.payments.wallet.model;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    @Test
    @DisplayName("Should create wallet with valid data")
    void shouldCreateWalletWithValidData() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet(walletId, user, BigDecimal.ZERO);

        assertEquals(walletId, wallet.getId());
        assertEquals(user, wallet.getUser());
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
    }

    @Test
    @DisplayName("Should update wallet fields")
    void shouldUpdateWalletFields() {
        Wallet wallet = new Wallet();
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        
        UUID walletId = UUID.randomUUID();
        wallet.setId(walletId);
        wallet.setUser(user);
        wallet.setBalance(new BigDecimal("100.00"));

        assertEquals(walletId, wallet.getId());
        assertEquals(user, wallet.getUser());
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        
        UUID walletId1 = UUID.randomUUID();
        UUID walletId2 = UUID.randomUUID();
        Wallet wallet1 = new Wallet(walletId1, user1, BigDecimal.TEN);
        Wallet wallet2 = new Wallet(walletId1, user1, BigDecimal.TEN);
        Wallet wallet3 = new Wallet(walletId2, user1, BigDecimal.ZERO);

        assertEquals(wallet1, wallet2);
        assertNotEquals(wallet1, wallet3);
        assertEquals(wallet1.hashCode(), wallet2.hashCode());
        assertNotEquals(wallet1.hashCode(), wallet3.hashCode());
    }
}
