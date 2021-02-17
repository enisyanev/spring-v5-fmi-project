package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.exchange.ExchangeRequest;
import com.project.spring.digitalwallet.dto.exchange.ExchangeResponse;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.transaction.Direction;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import com.project.spring.digitalwallet.model.transaction.Type;
import com.project.spring.digitalwallet.model.user.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

    private AccountService accountService;
    private TransactionService transactionService;
    private FxRatesService fxRatesService;
    private UserService userService;

    public ExchangeService(AccountService accountService, TransactionService transactionService,
            FxRatesService fxRatesService, UserService userService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.fxRatesService = fxRatesService;
        this.userService = userService;
    }

    public ExchangeResponse exchange(ExchangeRequest request) {
        long walletId = getLoggedUser().getWalletId();

        Account fromAccount = accountService.getByIdAndWalletId(request.getFromAccount(), walletId);

        Account toAccount = accountService.getByIdAndWalletId(request.getToAccount(), walletId);

        validate(fromAccount, request);

        Transaction fromTransaction =
            buildTransaction(request, fromAccount.getId(), Direction.W, fromAccount.getCurrency(),
                request.getAmount());
        Transaction toTransaction =
            buildTransaction(request, toAccount.getId(), Direction.D, toAccount.getCurrency(),
                fxRatesService
                    .getConvertedAmount(fromAccount.getCurrency(), toAccount.getCurrency(),
                        request.getAmount()));

        List<Transaction> transactions =
            transactionService.createTransactions(Arrays.asList(fromTransaction, toTransaction));

        return new ExchangeResponse(fromAccount.getId(), toAccount.getId(),
            TransactionStatus.PROCESSED, transactions.get(0).getSlipId());
    }
    
    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUsername(authentication.getName());
    }

    private Transaction buildTransaction(ExchangeRequest request, Long accountId,
                                         Direction direction, String currency, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(getLoggedUser().getWalletId());
        transaction.setAccountId(accountId);
        transaction.setDirection(direction);
        transaction.setType(Type.EXCHANGE);
        transaction.setCurrency(currency);
        transaction.setAmount(amount.setScale(2, RoundingMode.UP));
        transaction.setStatus(TransactionStatus.PROCESSED);

        return transaction;
    }

    private void validate(Account fromAccount, ExchangeRequest request) {
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidEntityDataException("Insufficient balance!");
        }
    }
}
