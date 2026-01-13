package com.rs.payments.wallet.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTypeTest {

    @Test
    void shouldHaveValues() {
        assertEquals(4, TransactionType.values().length);
        assertNotNull(TransactionType.valueOf("DEPOSIT"));
        assertNotNull(TransactionType.valueOf("WITHDRAWAL"));
        assertNotNull(TransactionType.valueOf("TRANSFER_IN"));
        assertNotNull(TransactionType.valueOf("TRANSFER_OUT"));
    }
}
