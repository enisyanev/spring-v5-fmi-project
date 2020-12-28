package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyRequest;
import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.transaction.Status;
import com.project.spring.digitalwallet.model.transaction.Transaction;
import com.project.spring.digitalwallet.model.transaction.Type;
import com.project.spring.digitalwallet.model.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

public class SendMoneyService {

    private WalletService walletService;
    private AccountService accountService;
    private TransactionService transactionService;

    public SendMoneyService(WalletService walletService, AccountService accountService,
                            TransactionService transactionService) {
        this.walletService = walletService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void sendMoney(SendMoneyRequest request) {
        User senderPrincipal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account sender = loadAccount(request.getAccountId(), senderPrincipal.getWalletId());
        validate(sender, request);
        Account recipient = loadRecipient(request.getWalletName(), request.getCurrency());

        // TODO: How to generate the SLIP_ID ? -> sequence ?
        Transaction senderTransaction = buildSenderTransaction(sender, request);
        Transaction recipientTransaction = buildRecipientTransaction(recipient, request);

        persistTransactions(senderTransaction, recipientTransaction);
    }

    private Account loadAccount(Long accountId, Long walletId) {
        Account account = accountService.getByIdAndWalletId(accountId, walletId);

        if (account == null) {
            throw new NonexistingEntityException(
                    String.format("Account with ID:%s does not exist for wallet id: %s.", accountId, walletId));
        }

        return account;
    }

    private void validate(Account account, SendMoneyRequest request) {
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InvalidEntityDataException("Insufficient balance!");
        }
    }

    private Account loadRecipient(String walletName, String currency) {
        try {
            WalletDto walletDto = walletService.getWalletDto(walletName);

            // Send money to the account with matching currency, if there is no such one fallback to the default account
            return walletDto.getAccounts().stream()
                    .filter(x -> x.getCurrency().equals(currency))
                    .findFirst()
                    .orElse(walletDto.getDefaultAccount());
        } catch (NonexistingEntityException e) {
            // Send money to non-registered
            // TODO: do we want to add additional complexity with this ??
            return null;
        }
    }

    private Transaction buildSenderTransaction(Account sender, SendMoneyRequest request) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(sender.getWalletId());
        transaction.setAccountId(sender.getId());
        transaction.setDirection("W");
        transaction.setType(Type.SEND_MONEY);
        transaction.setCurrency(request.getCurrency());
        transaction.setAmount(request.getAmount());
        // TODO: set to scheduled if we allow to send money to unregistered
        transaction.setStatus(Status.PROCESSED);

        return transaction;
    }

    private Transaction buildRecipientTransaction(Account recipient, SendMoneyRequest request) {
        Transaction transaction = new Transaction();
        transaction.setWalletId(recipient.getWalletId());
        transaction.setAccountId(recipient.getId());
        transaction.setDirection("D");
        transaction.setType(Type.SEND_MONEY);
        transaction.setCurrency(recipient.getCurrency());
        transaction.setAmount(request.getAmount());
        // TODO: set to scheduled if we allow to send money to unregistered
        transaction.setStatus(Status.PROCESSED);

        return transaction;
    }

    private void persistTransactions(Transaction senderTransaction, Transaction recipientTransaction) {
        transactionService.createTransactions(Arrays.asList(senderTransaction, recipientTransaction));
    }

}
