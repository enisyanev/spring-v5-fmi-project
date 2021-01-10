package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.ScheduledTransactionRepository;
import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyRequest;
import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyResponse;
import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.transaction.Direction;
import com.project.spring.digitalwallet.model.transaction.ScheduledTransaction;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import com.project.spring.digitalwallet.model.transaction.Type;
import com.project.spring.digitalwallet.model.user.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SendMoneyService {

    private WalletService walletService;
    private AccountService accountService;
    private TransactionService transactionService;
    private ScheduledTransactionRepository scheduledTransactionRepository;
    private UserService userService;
    private FxRatesService fxRatesService;

    public SendMoneyService(WalletService walletService, AccountService accountService,
                            TransactionService transactionService,
                            ScheduledTransactionRepository scheduledTransactionRepository,
                            UserService userService, FxRatesService fxRatesService) {
        this.walletService = walletService;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.userService = userService;
        this.fxRatesService = fxRatesService;
    }

    public SendMoneyResponse sendMoney(SendMoneyRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User senderPrincipal = userService.getUserByUsername(authentication.getName());
        Account sender =
            accountService
                .getByIdAndWalletId(request.getAccountId(), senderPrincipal.getWalletId());
        BigDecimal senderAmount = fxRatesService
            .getConvertedAmount(sender.getCurrency(), request.getCurrency(), request.getAmount())
            .setScale(2, RoundingMode.UP);

        validate(sender, senderAmount);
        Account recipient = loadRecipient(request.getWalletName(), request.getCurrency());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(buildTransaction(sender, Direction.W, request.getCurrency(),
            senderAmount,
            recipient == null ? TransactionStatus.SCHEDULED : TransactionStatus.PROCESSED));
        if (recipient != null) {
            BigDecimal recipientAmount = fxRatesService
                .getConvertedAmount(request.getCurrency(), recipient.getCurrency(),
                    request.getAmount());
            transactions.add(buildTransaction(recipient, Direction.D, recipient.getCurrency(),
                recipientAmount, TransactionStatus.PROCESSED));
        }

        List<Transaction> created = transactionService.createTransactions(transactions);

        Transaction senderTransaction = created.stream()
            .filter(
                x -> x.getWalletId().equals(sender.getWalletId()) && x.getType() == Type.SEND_MONEY)
            .findFirst().get();

        if (recipient == null) {
            handleScheduled(senderTransaction, request.getWalletName(), request);
        }

        return new SendMoneyResponse(senderTransaction.getSlipId(), senderTransaction.getWalletId(),
            senderTransaction.getAccountId(), senderTransaction.getStatus());
    }

    private void validate(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InvalidEntityDataException("Insufficient balance!");
        }
    }

    private Account loadRecipient(String walletName, String currency) {
        try {
            WalletDto walletDto = walletService.getWalletDto(walletName);

            // Send money to the account with matching currency,
            // if there is no such one fallback to the default account
            return walletDto.getAccounts().stream()
                .filter(x -> x.getCurrency().equals(currency))
                .findFirst()
                .orElse(walletDto.getDefaultAccount());
        } catch (NonexistingEntityException e) {
            // Send money to non-registered
            return null;
        }
    }

    private Transaction buildTransaction(Account account, Direction direction, String currency,
                                         BigDecimal amount,
                                         TransactionStatus status) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(account.getWalletId());
        transaction.setAccountId(account.getId());
        transaction.setDirection(direction);
        transaction.setType(Type.SEND_MONEY);
        transaction.setCurrency(currency);
        transaction.setAmount(amount.setScale(2, RoundingMode.UP));
        transaction.setStatus(status);

        return transaction;
    }

    private void handleScheduled(Transaction senderTransaction, String walletName,
                                 SendMoneyRequest request) {
        scheduledTransactionRepository
            .save(new ScheduledTransaction(walletName, senderTransaction.getSlipId(),
                request.getCurrency(), request.getAmount()));

        // TODO: Send email ?
    }

}
