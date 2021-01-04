package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.Wallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByName(String name);
}
