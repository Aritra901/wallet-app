package com.rs.payments.wallet.controller;

import com.rs.payments.wallet.dto.*;
import com.rs.payments.wallet.exception.ResourceNotFoundException;
import com.rs.payments.wallet.model.Wallet;
import com.rs.payments.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@Tag(name = "Wallet Management", description = "APIs for managing user wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(
            summary = "Create a new wallet for a user",
            description = "Creates a new wallet for the specified user ID with a zero balance.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Wallet created successfully",
                            content = @Content(schema = @Schema(implementation = Wallet.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createWallet(@Valid @RequestBody CreateWalletRequest request) {
/*        Wallet wallet = walletService.createWalletForUser(request.getUserId());
        return ResponseEntity.ok(wallet);*/

            Wallet wallet = walletService.createWalletForUser(request.getUserId());

            return ResponseEntity.status(HttpStatus.CREATED).body(wallet);


    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(
            @PathVariable UUID id,
            @Valid @RequestBody DepositRequest request) {

        try {
            Wallet updatedWallet = walletService.deposit(id, request.getAmount());
            return ResponseEntity.ok(updatedWallet);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Wallet not found");

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable UUID id,
            @Valid @RequestBody WithdrawRequest request) {

        try {
            Wallet updatedWallet = walletService.withdraw(id, request.getAmount());
            return ResponseEntity.ok(updatedWallet);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Wallet not found");

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }


    @PostMapping("/transfers")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {

        try {
            TransferResponse response = walletService.transfer(
                    request.getFromWalletId(),
                    request.getToWalletId(),
                    request.getAmount()
            );

            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());

        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getBalance(@PathVariable UUID id) {

        try {
            BigDecimal balance = walletService.getBalance(id);
            return ResponseEntity.ok(balance);

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Wallet not found");
        }
    }

}