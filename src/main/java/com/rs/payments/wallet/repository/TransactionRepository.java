package com.rs.payments.wallet.repository;

import java.util.UUID;
import com.rs.payments.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}