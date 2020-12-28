package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.AccountRepository;
import com.project.spring.digitalwallet.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getByIdAndWalletId(Long accountId, Long walletId) {
        return accountRepository.findByIdAndWalletId(accountId, walletId);
    }

    public List<Account> getByWalletId(Long walletId) {
        return accountRepository.findByWalletId(walletId);
    }

}
