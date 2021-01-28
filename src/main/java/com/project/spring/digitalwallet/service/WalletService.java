package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.WalletRepository;
import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.model.user.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private AccountService accountService;
    private WalletRepository walletRepo;
    private UserService userService;

    @Autowired
    public WalletService(AccountService accountService, WalletRepository walletRepo, UserService userService) {
        this.accountService = accountService;
        this.walletRepo = walletRepo;
        this.userService = userService;
    }

    public Wallet addWallet(Wallet wallet) {
        return walletRepo.save(wallet);
    }

    public Wallet getWalletById(Long id) {
        return walletRepo.findById(id).orElseThrow(() ->
            new NonexistingEntityException(String.format("Wallet with ID:%s does not exist.", id)));
    }

    public WalletDto getWalletDto(String walletName) {
        Wallet wallet = walletRepo.findByName(walletName).orElseThrow(() ->
            new NonexistingEntityException(
                String.format("Wallet with name:%s does not exist.", walletName)));

        List<Account> accounts = accountService.getByWalletId(wallet.getId());

        return new WalletDto(wallet, accounts);
    }

    public Wallet getWallet() {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return walletRepo.findById(user.getWalletId()).orElseThrow(() ->
            new NonexistingEntityException(String.format("Wallet with ID:%s does not exist.", user.getWalletId())));
    }
}
