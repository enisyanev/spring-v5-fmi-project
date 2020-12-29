package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.SlipRepository;
import com.project.spring.digitalwallet.dao.TransactionRepository;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.transaction.Direction;
import com.project.spring.digitalwallet.model.transaction.Slip;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private SlipRepository slipRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService,
                              SlipRepository slipRepository) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.slipRepository = slipRepository;
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() ->
                new NonexistingEntityException(String.format("Transaction with ID:%s does not exist.", id)));
    }

    public List<Transaction> createTransactions(List<Transaction> transactions) {
        List<Transaction> created = new ArrayList<>();
        Long slipId = generateSlipId();
        for (Transaction transaction : transactions) {
            BigDecimal amount = transaction.getAmount();
            if (transaction.getDirection() == Direction.W) {
                amount = amount.negate();
            }
            created.add(storeTransaction(transaction, slipId, amount));
        }

        return created;
    }

    private Long generateSlipId() {
        return slipRepository.save(new Slip()).getId();
    }

    @Transactional
    public Transaction storeTransaction(Transaction transaction, Long slipId, BigDecimal amount) {
        transaction.setSlipId(slipId);
        Transaction trn = transactionRepository.save(transaction);
        accountService.updateBalance(transaction.getAccountId(), amount);
        return trn;
    }

}
