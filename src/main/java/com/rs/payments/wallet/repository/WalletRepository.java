package com.rs.payments.wallet.repository;

import java.util.UUID;
import com.rs.payments.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}