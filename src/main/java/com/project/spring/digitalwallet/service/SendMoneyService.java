package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyRequest;
import com.project.spring.digitalwallet.model.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SendMoneyService {

    public void sendMoney(SendMoneyRequest request) {
        User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        loadAccount(request.getAccountId());
        validate(request);
        loadRecipient(request.getWalletName());

        // TODO: How to generate the SLIP_ID ? -> sequence ?
        buildSenderTransaction();
        buildRecipientTransaction();

        persistTransactions();
    }

    private void loadAccount(Long accountId) {
        // Load the account here, AccountService
        return;
    }

    private void validate(SendMoneyRequest request) {
        // validate the amount and other stuff here
    }

    private void loadRecipient(String walletName) {
        // load the recipient here
    }

    private void buildSenderTransaction() {
        // build the transaction on sender part
    }

    private void buildRecipientTransaction() {
        // build the transaction on recipient side
    }

    private void persistTransactions() {
        // pass the two transactions here
        // lock the accounts
        // insert the transactions
        // not sure if we want the insertion of the both transactions should be in one DB tranasction or we can insert it in separate transactions (maybe the second one)
    }

}
