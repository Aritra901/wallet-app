package com.rs.payments.wallet.model;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    @DisplayName("Should create transaction with data")
    void shouldCreateTransaction() {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        
        UUID txId = UUID.randomUUID();
        Transaction tx = new Transaction(txId, wallet, BigDecimal.TEN, TransactionType.DEPOSIT, now, "Test");

        assertEquals(txId, tx.getId());
        assertEquals(wallet, tx.getWallet());
        assertEquals(BigDecimal.TEN, tx.getAmount());
        assertEquals(TransactionType.DEPOSIT, tx.getType());
        assertEquals(now, tx.getTimestamp());
        assertEquals("Test", tx.getDescription());
    }

    @Test
    @DisplayName("Should update transaction fields")
    void shouldUpdateTransactionFields() {
        Transaction tx = new Transaction();
        Wallet wallet = new Wallet();
        LocalDateTime now = LocalDateTime.now();

        UUID txId = UUID.randomUUID();
        tx.setId(txId);
        tx.setWallet(wallet);
        tx.setAmount(BigDecimal.ONE);
        tx.setType(TransactionType.WITHDRAWAL);
        tx.setTimestamp(now);
        tx.setDescription("Desc");

        assertEquals(txId, tx.getId());
        assertEquals(wallet, tx.getWallet());
        assertEquals(BigDecimal.ONE, tx.getAmount());
        assertEquals(TransactionType.WITHDRAWAL, tx.getType());
        assertEquals(now, tx.getTimestamp());
        assertEquals("Desc", tx.getDescription());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Transaction tx1 = new Transaction();
        tx1.setId(id1);
        Transaction tx2 = new Transaction();
        tx2.setId(id1);
        Transaction tx3 = new Transaction();
        tx3.setId(id2);

        assertEquals(tx1, tx2);
        assertNotEquals(tx1, tx3);
        assertEquals(tx1.hashCode(), tx2.hashCode());
    }
}
