package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.AccountRepository;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NonexistingEntityException(String.format("Account with ID:%s does not exist.",
                        accountId)));
    }

    public Account getByIdAndWalletId(Long accountId, Long walletId) {
        return accountRepository.findByIdAndWalletId(accountId, walletId);
    }

    public List<Account> getByWalletId(Long walletId) {
        return accountRepository.findByWalletId(walletId);
    }

    // This won't scale well but we expect 1 user for now :)
    // Better would be to acquire lock on given account
    public synchronized void updateBalance(Long accountId, BigDecimal amount) {
        Account account = getById(accountId);
        if (account.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidEntityDataException("Not enough balance!");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

}
