package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.RecurringPaymentsRepository;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.recurringpayments.UpdateRecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyRequest;
import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.model.recurring.RecurringPayment;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecurringPaymentService {

    private AccountService accountService;
    private WalletService walletService;
    private RecurringPaymentsRepository recurringPaymentsRepository;
    private SendMoneyService sendMoneyService;

    public RecurringPaymentService(AccountService accountService, WalletService walletService,
                                   RecurringPaymentsRepository recurringPaymentsRepository,
                                   SendMoneyService sendMoneyService) {
        this.accountService = accountService;
        this.walletService = walletService;
        this.recurringPaymentsRepository = recurringPaymentsRepository;
        this.sendMoneyService = sendMoneyService;
    }

    public void createRecurringPayment(RecurringPaymentRequest request) {
        Wallet sender = walletService.getWallet();
        WalletDto recipient = validate(request, sender.getId());

        RecurringPayment recurringPayment = buildRecurringPayment(request, recipient, sender.getId());
        recurringPaymentsRepository.save(recurringPayment);
    }

    private WalletDto validate(RecurringPaymentRequest request, Long senderWalletId) {
        accountService.getByIdAndWalletId(request.getAccountId(), senderWalletId);
        return walletService.getWalletDto(request.getWalletName());
    }

    private RecurringPayment buildRecurringPayment(RecurringPaymentRequest request,
                                                   WalletDto recipient, Long senderWalletId) {
        RecurringPayment recurringPayment = new RecurringPayment();
        recurringPayment.setWalletId(senderWalletId);
        recurringPayment.setAccountId(request.getAccountId());
        recurringPayment.setRecipientId(recipient.getWallet().getId());
        recurringPayment.setAmount(request.getAmount());
        recurringPayment.setNextExecutionTime(request.getStartDate());
        recurringPayment.setPeriod(request.getPeriod());
        recurringPayment.setActive(true);

        return recurringPayment;
    }

    public void executeRecurringPayments() {
        List<RecurringPayment> paymentsToExecute = recurringPaymentsRepository.findAllForExecution();
        paymentsToExecute.forEach(this::executePayment);
    }

    private void executePayment(RecurringPayment payment) {
        SendMoneyRequest request = buildSendMoneyRequest(payment);

        sendMoneyService.sendMoney(request);

        payment.setLastExecutionTime(LocalDate.now());
        payment.setNextExecutionTime(payment.getPeriod().getNextDate());

        recurringPaymentsRepository.save(payment);
    }

    private SendMoneyRequest buildSendMoneyRequest(RecurringPayment payment) {
        Account account = accountService.getById(payment.getAccountId());
        Wallet recipient = walletService.getWalletById(payment.getRecipientId());

        SendMoneyRequest request = new SendMoneyRequest();
        //request.setWalletId(payment.getWalletId());
        request.setAccountId(payment.getAccountId());
        request.setAmount(payment.getAmount());
        request.setCurrency(account.getCurrency());
        request.setWalletName(recipient.getName());
        return request;
    }

    public List<RecurringPayment> getRecurringPayments() {
        // TODO: should we return only active ones ?
        return recurringPaymentsRepository.findAllByWalletId(walletService.getWallet().getId());
    }

    public void updateRecurringPayment(Long id, UpdateRecurringPaymentRequest request) {

        Long walletId = walletService.getWallet().getId();
        RecurringPayment payment = recurringPaymentsRepository.findByIdAndWalletId(id, walletId)
                .orElseThrow(() -> new NonexistingEntityException(
                        String.format("Recurring payment with ID:%s for wallet with ID:%s does not exist.",
                                id, walletId)));

        payment.setActive(request.getActive());
        payment.setAmount(request.getAmount());
        payment.setPeriod(request.getPeriod());

        recurringPaymentsRepository.save(payment);
    }
}
