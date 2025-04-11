package apptest.javacode.javacodetesstapp.service;

import apptest.javacode.javacodetesstapp.entity.Wallet;
import apptest.javacode.javacodetesstapp.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTests {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private Wallet wallet;
    private UUID walletId;

    @BeforeEach
    public void setUp() {
        walletId = UUID.randomUUID();
        wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(100));
    }

    @Test
    public void testUpdateBalanceDeposit(){
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.updateBalance(walletId, "DEPOSIT", BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testUpdateBalanceWithdrawSuccess() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.updateBalance(walletId, "WITHDRAW", BigDecimal.valueOf(30));

        assertEquals(BigDecimal.valueOf(70), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    public void testUpdateBalanceWithdrawInsufficientFunds() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            walletService.updateBalance(walletId, "WITHDRAW", BigDecimal.valueOf(150));
        });

        assertEquals("Withdrawal not possible", exception.getMessage());
        verify(walletRepository, never()).save(wallet);
    }

    @Test
    public void testUpdateBalanceInvalidOperationType() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            walletService.updateBalance(walletId, "INVALID_OPERATION", BigDecimal.valueOf(50));
        });

        assertEquals("Invalid operation type", exception.getMessage());
        verify(walletRepository, never()).save(wallet);
    }

    @Test
    public void testGetBalance() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        BigDecimal balance = walletService.getBalance(walletId);

        assertEquals(wallet.getBalance(), balance);
    }

    @Test
    public void testGetBalanceWalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            walletService.getBalance(walletId);
        });

        assertEquals("Wallet not found", exception.getMessage());
    }

    @Test
    public void testCreateWallet() {
        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        BigDecimal initialBalance = BigDecimal.valueOf(200);
        Wallet createdWallet = walletService.createWallet(initialBalance);

        assertEquals(initialBalance, createdWallet.getBalance());
        verify(walletRepository).save(any(Wallet.class));
    }
}
