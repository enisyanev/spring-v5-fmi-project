package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.Account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByWalletId(Long walletId);

    Optional<Account> findByCurrencyAndWalletId(String currency, Long walletId);

    Optional<Account> findByIdAndWalletId(Long accountId, Long walletId);
}
