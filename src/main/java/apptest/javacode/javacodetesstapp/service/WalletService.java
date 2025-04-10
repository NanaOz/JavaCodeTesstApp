package apptest.javacode.javacodetesstapp.service;

import apptest.javacode.javacodetesstapp.entity.Wallet;
import apptest.javacode.javacodetesstapp.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void updateBalance(UUID walletId, String operationType, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        if (operationType.equals("DEPOSIT")) {
            wallet.setBalance(wallet.getBalance().add(amount));
        } else if (operationType.equals("WITHDRAW")) {
            if (wallet.getBalance().compareTo(amount) < 0) {
                throw new EntityNotFoundException("Withdrawal not possible");
            }
            wallet.setBalance(wallet.getBalance().subtract(amount));
        } else {
            throw new IllegalArgumentException("Invalid operation type");
        }
        walletRepository.save(wallet);
    }

    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Wallet not found"));
        return wallet.getBalance();
    }

    public Wallet createWallet(BigDecimal initialBalance) {
        Wallet wallet = new Wallet();
        wallet.setBalance(initialBalance);
        return walletRepository.save(wallet);
    }

}
