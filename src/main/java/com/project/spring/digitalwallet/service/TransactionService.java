package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.TransactionRepository;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() ->
                new NonexistingEntityException(String.format("Transaction with ID:%s does not exist.", id)));
    }

    @Transactional
    public void createTransactions(List<Transaction> transactions) {
        // pass the two transactions here
        // lock the accounts
        // insert the transactions
        // not sure if we want the insertion of the both transactions should be in one DB tranasction or we can insert it in separate transactions (maybe the second one)
        Long slipId = generateSlipId();
        transactions.forEach(x -> x.setSlipId(slipId));
        transactionRepository.saveAll(transactions);
        // TODO: update the account balances
        // TODO: FEES??
    }

    // How to generate the Slip ID?
    private Long generateSlipId() {
        return 1L;
    }

}
