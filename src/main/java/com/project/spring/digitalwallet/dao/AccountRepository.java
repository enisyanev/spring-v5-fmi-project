package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByWalletId(Long walletId);
    Account findByIdAndWalletId(Long accountId, Long walletId);
}
