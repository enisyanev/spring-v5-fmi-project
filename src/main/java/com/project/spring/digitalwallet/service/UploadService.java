package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.upload.PaymentInstrumentType;
import com.project.spring.digitalwallet.dto.upload.UploadRequest;
import com.project.spring.digitalwallet.dto.upload.UploadResponse;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.card.Card;
import com.project.spring.digitalwallet.model.card.CardType;
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
public class UploadService {

    private UserService userService;
    private AccountService accountService;
    private CardService cardService;
    private BankService bankService;
    private FxRatesService fxRatesService;
    private TransactionService transactionService;

    public UploadService(UserService userService, AccountService accountService,
                         CardService cardService, BankService bankService,
                         FxRatesService fxRatesService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.cardService = cardService;
        this.bankService = bankService;
        this.fxRatesService = fxRatesService;
        this.transactionService = transactionService;
    }

    public UploadResponse upload(UploadRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        Account account =
            accountService.getByIdAndWalletId(request.getAccountId(), user.getWalletId());

        Type transactionType = getTransactionType(request, user.getWalletId());

        // Convert to accounts currency and store it in that currency
        BigDecimal amount = fxRatesService.getConvertedAmount(request.getCurrency(),
            account.getCurrency(), request.getAmount());
        Transaction transaction = buildTransaction(account, transactionType, amount);

        List<Transaction> transactions =
            transactionService.createTransactions(Collections.singletonList(transaction));

        Transaction created = transactions.get(0);

        return new UploadResponse(request.getAccountId(), created.getStatus(), created.getSlipId());
    }

    private Type getTransactionType(UploadRequest request, long walletId) {

        if (request.getType() == PaymentInstrumentType.CARD) {
            Card card = cardService.getByIdAndWalletId(request.getInstrumentId(), walletId);

            return card.getCardType() == CardType.VISA ? Type.VISA : Type.MASTERCARD;
        }

        if (request.getType() == PaymentInstrumentType.BANK_ACCOUNT) {
            // use it just for validation
            bankService.getByIdAndWalletId(request.getInstrumentId(), walletId);

            return Type.BANKWIRE;
        }

        throw new InvalidEntityDataException("Wrong instrument type!");
    }

    private Transaction buildTransaction(Account account, Type transactionType,
                                         BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(account.getWalletId());
        transaction.setAccountId(account.getId());
        transaction.setDirection(Direction.D);
        transaction.setType(transactionType);
        transaction.setAmount(amount);
        transaction.setCurrency(account.getCurrency());
        transaction.setStatus(TransactionStatus.PROCESSED);

        return transaction;
    }

}
