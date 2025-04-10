package apptest.javacode.javacodetesstapp.controller;

import apptest.javacode.javacodetesstapp.dto.WalletOperation;
import apptest.javacode.javacodetesstapp.entity.Wallet;
import apptest.javacode.javacodetesstapp.service.WalletService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<String> updateBalance(@RequestBody WalletOperation walletOperation) {
        try {
            walletService.updateBalance(walletOperation.getWalletId(), walletOperation.getOperationType(),
                    walletOperation.getAmount());
            return ResponseEntity.accepted().body("Balance updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {
        try {
            BigDecimal balance = walletService.getBalance(walletId);
            return ResponseEntity.ok(balance);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNewWallet(@RequestParam BigDecimal initialBalance) {
        Wallet wallet = walletService.createWallet(initialBalance);
        return ResponseEntity.accepted().body("Wallet created successfully. Wallet ID: " + wallet.getId());
    }

}
