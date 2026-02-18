package com.rs.payments.wallet.service.impl;

import com.rs.payments.wallet.dto.TransferResponse;
import com.rs.payments.wallet.exception.ResourceNotFoundException;
import com.rs.payments.wallet.model.Transaction;
import com.rs.payments.wallet.model.TransactionType;
import com.rs.payments.wallet.model.User;
import com.rs.payments.wallet.model.Wallet;
import com.rs.payments.wallet.repository.TransactionRepository;
import com.rs.payments.wallet.repository.UserRepository;
import com.rs.payments.wallet.repository.WalletRepository;
import com.rs.payments.wallet.service.WalletService;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletServiceImpl(UserRepository userRepository, WalletRepository walletRepository , TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public Wallet createWalletForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (walletRepository.existsByUser(user)){
            throw new IllegalStateException("Wallet already exists");
        }

        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUser(user);
        user.setWallet(wallet);

        user = userRepository.save(user); // Cascade saves wallet
        return user.getWallet();
    }

    @Override
    @Transactional
    public Wallet deposit(UUID walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        // Update balance
        wallet.setBalance(wallet.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT); // Deposit = CREDIT
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription("Wallet deposit");

        transactionRepository.save(transaction);

        return wallet;
    }

    @Override
    @Transactional
    public Wallet withdraw(UUID walletId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        // Check sufficient balance
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Deduct balance
        wallet.setBalance(wallet.getBalance().subtract(amount));

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAWAL); // Withdraw = DEBIT
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription("Wallet withdrawal");

        transactionRepository.save(transaction);

        return wallet;
    }

    @Override
    @Transactional
    public TransferResponse transfer(UUID fromWalletId, UUID toWalletId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (fromWalletId.equals(toWalletId)) {
            throw new IllegalArgumentException("Cannot transfer to same wallet");
        }

        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender wallet not found"));

        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver wallet not found"));

        // Check sufficient funds
        if (fromWallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Perform balance updates
        fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
        toWallet.setBalance(toWallet.getBalance().add(amount));

        // Transaction 1 — TRANSFER OUT
        Transaction debitTxn = new Transaction();
        debitTxn.setWallet(fromWallet);
        debitTxn.setAmount(amount);
        debitTxn.setType(TransactionType.TRANSFER_OUT); // TRANSFER_OUT
        debitTxn.setTimestamp(LocalDateTime.now());
        debitTxn.setDescription("Transfer to wallet " + toWalletId);

        // Transaction 2 — TRANSFER IN
        Transaction creditTxn = new Transaction();
        creditTxn.setWallet(toWallet);
        creditTxn.setAmount(amount);
        creditTxn.setType(TransactionType.TRANSFER_IN); // TRANSFER_IN
        creditTxn.setTimestamp(LocalDateTime.now());
        creditTxn.setDescription("Transfer from wallet " + fromWalletId);

        transactionRepository.save(debitTxn);
        transactionRepository.save(creditTxn);

        return new TransferResponse(
                fromWalletId,
                toWalletId,
                amount,
                fromWallet.getBalance(),
                toWallet.getBalance()
        );
    }
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(UUID walletId) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        return wallet.getBalance();
    }
}