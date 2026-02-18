package com.rs.payments.wallet.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {

    private UUID fromWalletId;
    private UUID toWalletId;
    private BigDecimal amount;
    private BigDecimal fromBalance;
    private BigDecimal toBalance;

}
