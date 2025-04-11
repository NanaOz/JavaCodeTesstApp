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

    /**
     * Обновляет баланс указанного кошелька в зависимости от типа операции (DEPOSIT или WITHDRAW).
     *
     * @param walletId      идентификатор кошелька, который необходимо обновить
     * @param operationType тип операции (DEPOSIT или WITHDRAW)
     * @param amount        сумма, которую необходимо внести или вывести
     */
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

    /**
     * Получает текущий баланс указанного кошелька.
     *
     * @param walletId идентификатор кошелька, для которого необходимо получить баланс
     * @return текущий баланс кошелька
     */
    public BigDecimal getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new EntityNotFoundException("Wallet not found"));
        return wallet.getBalance();
    }

    /**
     * Создает новый кошелек с заданным начальным балансом.
     *
     * @param initialBalance начальный баланс для нового кошелька
     * @return созданный кошелек
     */
    public Wallet createWallet(BigDecimal initialBalance) {
        Wallet wallet = new Wallet();
        wallet.setBalance(initialBalance);
        return walletRepository.save(wallet);
    }
}
