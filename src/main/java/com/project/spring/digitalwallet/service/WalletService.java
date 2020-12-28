package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.spring.digitalwallet.dao.WalletRepository;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Wallet;

import java.util.List;

@Service
public class WalletService {

    private AccountService accountService;
	private WalletRepository walletRepo;

    @Autowired
    public WalletService(AccountService accountService, WalletRepository walletRepo) {
        this.accountService = accountService;
        this.walletRepo = walletRepo;
    }
    
    public Wallet addWallet(Wallet wallet) {
    	return walletRepo.save(wallet);
    }
    
    public Wallet getWalletById(String id) { 	
    	return walletRepo.findById(Long.parseLong(id, 10) ).orElseThrow(() ->
           new NonexistingEntityException(String.format("Wallet with ID:%s does not exist.", id)));
    }

    public WalletDto getWalletDto(String walletName) {
        Wallet wallet = walletRepo.findByName(walletName).orElseThrow(() ->
                new NonexistingEntityException(String.format("Wallet with name:%s does not exist.", walletName)));

        List<Account> accounts = accountService.getByWalletId(wallet.getId());

        return new WalletDto(wallet, accounts);
    }
}
