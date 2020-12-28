package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.TransactionRepository;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import org.springframework.stereotype.Service;

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

}
