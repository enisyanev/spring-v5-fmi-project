package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.ScheduledTransactionRepository;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.transaction.Direction;
import com.project.spring.digitalwallet.model.transaction.ScheduledTransaction;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import com.project.spring.digitalwallet.model.transaction.Type;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class ScheduledSendMoneyService {

    private ScheduledTransactionRepository scheduledTransactionRepository;
    private FxRatesService fxRatesService;
    private TransactionService transactionService;

    public ScheduledSendMoneyService(ScheduledTransactionRepository scheduledTransactionRepository,
                                     FxRatesService fxRatesService, TransactionService transactionService) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.fxRatesService = fxRatesService;
        this.transactionService = transactionService;
    }

    @Transactional
    public void executeScheduledSendMoneys(String email, Account account) {
        List<ScheduledTransaction> transactions = scheduledTransactionRepository.findByEmail(email);

        if (CollectionUtils.isEmpty(transactions)) {
            return;
        }

        transactions.forEach(trn -> executeTransaction(trn, account));
    }

    public void executeTransaction(ScheduledTransaction scheduledTransaction, Account account) {
        Transaction transaction = buildTransaction(scheduledTransaction, account);
        Transaction otherTransaction = transactionService.getBySlipId(scheduledTransaction.getSlipId());
        transactionService.storeTransaction(transaction, scheduledTransaction.getSlipId(), transaction.getAmount());
        transactionService.updateStatus(otherTransaction.getId(), TransactionStatus.PROCESSED);
        scheduledTransactionRepository.delete(scheduledTransaction);
    }

    private Transaction buildTransaction(ScheduledTransaction scheduledTransaction, Account account) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(account.getWalletId());
        transaction.setAccountId(account.getId());
        transaction.setDirection(Direction.D);
        transaction.setType(Type.SEND_MONEY);
        transaction.setCurrency(account.getCurrency());
        transaction.setAmount(fxRatesService
            .getConvertedAmount(scheduledTransaction.getCurrency(), account.getCurrency(),
                scheduledTransaction.getAmount()));
        transaction.setStatus(TransactionStatus.PROCESSED);

        return transaction;
    }

}
