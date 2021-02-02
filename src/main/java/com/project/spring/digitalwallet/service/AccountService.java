package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.AccountRepository;
import com.project.spring.digitalwallet.dto.account.AccountRequest;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.user.User;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;
    private UserService userService;

    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    public Account getById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NonexistingEntityException(
                        String.format("Account with ID:%s does not exist.",
                                accountId)));
    }

    public Account createAccount(Account account) {
        return this.accountRepository.save(account);
    }

    public void createAccountWithUser(AccountRequest account) {
        User user = userService.getUserByUsername(account.getUsername());
        Account acc = new Account(user.getWalletId(), account.getCurrency());
        this.accountRepository.save(acc);
    }

    public Account getByIdAndWalletId(Long accountId, Long walletId) {
        return accountRepository.findByIdAndWalletId(accountId, walletId)
                .orElseThrow(() -> new NonexistingEntityException(
                        String.format("Account with ID:%s for wallet with ID:%s does not exist.",
                                accountId, walletId)));
    }

    public Account getByCurrencyAndWalletId(String currency, Long walletId) {
        return accountRepository.findByCurrencyAndWalletId(currency, walletId)
                .orElseThrow(() -> new NonexistingEntityException(
                        String.format("Account with Currency:%s for wallet with ID:%s does not exist.",
                                currency, walletId)));
    }

    public List<Account> getByWalletId(Long walletId) {
        return accountRepository.findByWalletId(walletId);
    }

    public List<Account> getByWalletIdUsingUsername(String username) {
        User user = userService.getUserByUsername(username);
        return accountRepository.findByWalletId(user.getWalletId());
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
