package apptest.javacode.javacodetesstapp.repository;

import apptest.javacode.javacodetesstapp.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}
