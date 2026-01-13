package com.rs.payments.wallet.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transaction entity")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique identifier of the transaction", example = "b1f8e321-7c9b-46e2-8d1a-4f5a6b7c8d9e")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @Schema(description = "Wallet associated with the transaction")
    private Wallet wallet;

    @Schema(description = "Amount of the transaction", example = "50.00")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of the transaction (DEBIT/CREDIT)")
    private TransactionType type;

    @Schema(description = "Timestamp of when the transaction occurred")
    private LocalDateTime timestamp;

    @Schema(description = "Description of the transaction", example = "Payment for services")
    private String description;
}