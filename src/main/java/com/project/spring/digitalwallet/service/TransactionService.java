package com.project.spring.digitalwallet.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.spring.digitalwallet.dao.SlipRepository;
import com.project.spring.digitalwallet.dao.TransactionRepository;
import com.project.spring.digitalwallet.dto.transaction.TransactionDto;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.transaction.Direction;
import com.project.spring.digitalwallet.model.transaction.Slip;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import com.project.spring.digitalwallet.model.user.User;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountService accountService;
    private SlipRepository slipRepository;
    private UserService userService;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountService accountService,
                              SlipRepository slipRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.slipRepository = slipRepository;
        this.userService = userService;
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() ->
            new NonexistingEntityException(
                String.format("Transaction with ID:%s does not exist.", id)));
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
        accountService.updateBalance(transaction.getAccountId(), amount);
        return transactionRepository.save(transaction);
    }
    
    public List<TransactionDto> getTransactionsHistory(int pageNo, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Transaction> transactionHistory = transactionRepository.findByWalletId(user.getWalletId(), pageable)
                .getContent();
        return transactionHistory.stream().map(t -> new TransactionDto(t.getId(), t.getAmount(), t.getCreatedTime(),
                t.getAccountId(), t.getDirection(), t.getCurrency())).collect(Collectors.toList());
    }

}
