package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.BankRepository;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Bank;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    private BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Bank createBank(Bank bank) {
        return bankRepository.save(bank);
    }

    public List<Bank> getBankByWalletId(long walletId) {
        return bankRepository.findByWalletId(walletId);
    }

    public Bank getByIdAndWalletId(long id, long walletId) {
        return bankRepository.findByIdAndWalletId(id, walletId).orElseThrow(
            () -> new NonexistingEntityException(
                String.format("Bank account with ID:%s for wallet %s does not exist.",
                    id, walletId)));
    }

}
