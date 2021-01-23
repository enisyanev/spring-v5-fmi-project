package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.RecurringPaymentsRepository;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.model.recurring.RecurringPayment;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecurringPaymentService {

    private AccountService accountService;
    private WalletService walletService;
    private RecurringPaymentsRepository recurringPaymentsRepository;

    public RecurringPaymentService(AccountService accountService, WalletService walletService,
                                   RecurringPaymentsRepository recurringPaymentsRepository) {
        this.accountService = accountService;
        this.walletService = walletService;
        this.recurringPaymentsRepository = recurringPaymentsRepository;
    }

    public void createRecurringPayment(RecurringPaymentRequest request) {
        WalletDto recipient = validate(request);

        RecurringPayment recurringPayment = buildRecurringPayment(request, recipient);
        recurringPaymentsRepository.save(recurringPayment);
    }

    private WalletDto validate(RecurringPaymentRequest request) {
        accountService.getByIdAndWalletId(request.getAccountId(), request.getCustomerId());
        return walletService.getWalletDto(request.getWalletName());
    }

    private RecurringPayment buildRecurringPayment(RecurringPaymentRequest request,
                                                   WalletDto recipient) {
        RecurringPayment recurringPayment = new RecurringPayment();
        recurringPayment.setWalletId(request.getCustomerId());
        recurringPayment.setAccountId(request.getAccountId());
        recurringPayment.setRecipientId(recipient.getWallet().getId());
        recurringPayment.setAmount(request.getAmount());
        recurringPayment.setNextExecutionTime(request.getStartDate());
        recurringPayment.setPeriod(request.getPeriod());
        recurringPayment.setActive(true);

        return recurringPayment;
    }

    public void executeRecurringPayments() {
        List<RecurringPayment> paymentsToExecute =
            recurringPaymentsRepository.findAllForExecution();
        paymentsToExecute.forEach(this::executePayment);
    }

    private void executePayment(RecurringPayment payment) {

        // TODO: make a payment

        payment.setLastExecutionTime(LocalDate.now());
        payment.setNextExecutionTime(payment.getPeriod().getNextDate());

        recurringPaymentsRepository.save(payment);
    }

}
