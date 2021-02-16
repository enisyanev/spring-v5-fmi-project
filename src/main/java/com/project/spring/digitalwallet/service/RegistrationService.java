package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.registration.AddUserDto;
import com.project.spring.digitalwallet.dto.registration.RegistrationDto;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.model.user.User;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private UserService userService;
    private WalletService walletService;
    private AccountService accountService;
    private ScheduledSendMoneyService scheduledSendMoneyService;

    public RegistrationService(UserService userService, WalletService walletService, AccountService accountService,
                               ScheduledSendMoneyService scheduledSendMoneyService) {
        this.userService = userService;
        this.walletService = walletService;
        this.accountService = accountService;
        this.scheduledSendMoneyService = scheduledSendMoneyService;
    }

    @Transactional
    public User register(RegistrationDto request) {
        if (walletService.exists(request.getEmail())) {
            throw new InvalidEntityDataException("There is already registered wallet with this email in the system!");
        }
        if (userService.exist(request.getUsername())) {
            throw new InvalidEntityDataException("There is already registered user with this username in the system!");
        }
        Wallet wallet = new Wallet(request.getEmail());
        Wallet createdWallet = walletService.addWallet(wallet);

        Account newAccount = new Account(createdWallet.getId(), request.getCurrency());
        accountService.createAccount(newAccount);

        User newUser = new User(request, createdWallet.getId());
        User createdUser = userService.addUser(newUser);

        scheduledSendMoneyService.executeScheduledSendMoneys(request.getEmail(), newAccount);

        return createdUser;
    }

    @Transactional
    public List<User> addUser(AddUserDto request) {
        if (userService.exist(request.getUsername())) {
            throw new InvalidEntityDataException("There is already registered user with this username in the system!");
        }
        long walletId = getLoggedUser().getId();

        User newUser = new User(request, walletId);
        User createdUser = userService.addUser(newUser);

        return userService.getByWalletId(walletId);
    }

    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUsername(authentication.getName());
    }

}
