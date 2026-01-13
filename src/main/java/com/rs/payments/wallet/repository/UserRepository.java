package com.rs.payments.wallet.repository;

import java.util.UUID;
import com.rs.payments.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
}