package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.registration.RegistrationDto;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.model.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private UserService userService;
    private WalletService walletService;
    private AccountService accountService;

    public RegistrationService(UserService userService, WalletService walletService, AccountService accountService) {
        this.userService = userService;
        this.walletService = walletService;
        this.accountService = accountService;
    }

    @Transactional
    public User register(RegistrationDto request) {
        Wallet wallet = new Wallet(request.getEmail());
        Wallet createdWallet = walletService.addWallet(wallet);

        Account newAccount = new Account(createdWallet.getId(), request.getCurrency());
        accountService.createAccount(newAccount);

        User newUser = new User(request, createdWallet.getId());
        return userService.addUser(newUser);
    }

}
