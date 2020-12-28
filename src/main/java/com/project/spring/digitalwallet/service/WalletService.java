package com.project.spring.digitalwallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.spring.digitalwallet.dao.WalletRepository;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Wallet;

@Service
public class WalletService {
	private WalletRepository walletRepo;

    @Autowired
    public WalletService(WalletRepository walletRepo) {
        this.walletRepo = walletRepo;
    }
    
    public Wallet addWallet(Wallet wallet) {
    	return walletRepo.save(wallet);
    }
    
    public Wallet getWalletById(String id) { 	
    	return walletRepo.findById(Long.parseLong(id, 10) ).orElseThrow(() ->
           new NonexistingEntityException(String.format("Wallet with ID:%s does not exist.", id)));
    }
}
