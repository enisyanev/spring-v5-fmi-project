package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
	
	@Autowired
	private WalletService walletService;
	
	@GetMapping("/{id}")
    public Wallet getWalletById(@PathVariable("id") String id) {
        return walletService.getWalletById(id);
    }

	@PostMapping
	public Wallet createWallet(@Valid @RequestBody Wallet wallet) {
        return walletService.addWallet(wallet);
    } 
}
