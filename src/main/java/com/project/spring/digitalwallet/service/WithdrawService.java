package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.upload.MoneyRequest;
import com.project.spring.digitalwallet.dto.upload.MoneyResponse;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.transaction.Direction;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import com.project.spring.digitalwallet.model.transaction.Type;
import com.project.spring.digitalwallet.model.user.User;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {

    private UserService userService;
    private AccountService accountService;
    private TransactionService transactionService;
    private FxRatesService fxRatesService;
    private PaymentInstrumentService paymentInstrumentService;

    public WithdrawService(UserService userService, AccountService accountService,
                           TransactionService transactionService,
                           FxRatesService fxRatesService, PaymentInstrumentService paymentInstrumentService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.fxRatesService = fxRatesService;
        this.paymentInstrumentService = paymentInstrumentService;
    }

    public MoneyResponse withdraw(MoneyRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        Account account =
            accountService.getByIdAndWalletId(request.getAccountId(), user.getWalletId());

        validate(request, account);

        Type transactionType = paymentInstrumentService.decideTransactionType(request, user.getWalletId());

        // Convert to accounts currency and store it in that currency
        BigDecimal amount = fxRatesService.getConvertedAmount(request.getCurrency(),
            account.getCurrency(), request.getAmount());
        Transaction transaction = buildTransaction(account, transactionType, amount);

        List<Transaction> transactions =
            transactionService.createTransactions(Collections.singletonList(transaction));

        Transaction created = transactions.get(0);

        return new MoneyResponse(request.getAccountId(), created.getStatus(), created.getSlipId());
    }

    private void validate(MoneyRequest request, Account account) {
        BigDecimal requestedAmount =
            fxRatesService.getConvertedAmount(account.getCurrency(), request.getCurrency(),
                request.getAmount());
        if (account.getBalance().compareTo(requestedAmount) < 0) {
            throw new InvalidEntityDataException("Not enough balance!");
        }
    }

    private Transaction buildTransaction(Account account, Type transactionType,
                                         BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(account.getWalletId());
        transaction.setAccountId(account.getId());
        transaction.setDirection(Direction.W);
        transaction.setType(transactionType);
        transaction.setAmount(amount);
        transaction.setCurrency(account.getCurrency());
        transaction.setStatus(TransactionStatus.PROCESSED);

        return transaction;
    }

}
