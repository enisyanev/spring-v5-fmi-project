package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.RecurringPaymentsRepository;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentResponse;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPeriod;
import com.project.spring.digitalwallet.dto.recurringpayments.UpdateRecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyRequest;
import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.model.recurring.RecurringPayment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.project.spring.digitalwallet.model.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RecurringPaymentService {

    private AccountService accountService;
    private WalletService walletService;
    private RecurringPaymentsRepository recurringPaymentsRepository;
    private SendMoneyService sendMoneyService;
    private UserService userService;

    public RecurringPaymentService(AccountService accountService, WalletService walletService,
                                   RecurringPaymentsRepository recurringPaymentsRepository,
                                   SendMoneyService sendMoneyService, UserService userService) {
        this.accountService = accountService;
        this.walletService = walletService;
        this.recurringPaymentsRepository = recurringPaymentsRepository;
        this.sendMoneyService = sendMoneyService;
        this.userService = userService;
    }

    public void createRecurringPayment(RecurringPaymentRequest request) {
        Wallet sender = walletService.getWallet();
        WalletDto recipient = validate(request, sender.getId());

        RecurringPayment recurringPayment = buildRecurringPayment(request, recipient, sender.getId());
        recurringPaymentsRepository.save(recurringPayment);
    }

    private WalletDto validate(RecurringPaymentRequest request, Long senderWalletId) {
        User user = userService.getUserByEmail(request.getEmail());
        accountService.getByIdAndWalletId(request.getAccountId(), senderWalletId);
        Wallet wallet = walletService.getWalletById(user.getWalletId());
        return walletService.getWalletDto(wallet.getName());
    }

    private RecurringPayment buildRecurringPayment(RecurringPaymentRequest request,
                                                   WalletDto recipient, Long senderWalletId) {
        RecurringPayment recurringPayment = new RecurringPayment();
        recurringPayment.setWalletId(senderWalletId);
        recurringPayment.setAccountId(request.getAccountId());
        recurringPayment.setRecipientId(recipient.getWallet().getId());
        recurringPayment.setAmount(request.getAmount());
        recurringPayment.setRecipient(request.getEmail());
        recurringPayment.setNextExecutionTime(LocalDate.now());
        recurringPayment.setPeriod(request.getPeriod());
        recurringPayment.setActive(true);

        return recurringPayment;
    }

    public void executeRecurringPayments() {
        List<RecurringPayment> paymentsToExecute = recurringPaymentsRepository.findAllForExecution();
        List<RecurringPayment> forExecution = findAllForExecution();
        for (int i = 0; i < forExecution.size(); i++) {
            RecurringPayment payment = forExecution.get(i);
            executePayment(payment);
            if (payment.getPeriod() == RecurringPeriod.DAILY) {
                payment.setLastExecutionTime(payment.getNextExecutionTime());
                payment.setNextExecutionTime(payment.getNextExecutionTime().plusDays(1));
            } else if (payment.getPeriod() == RecurringPeriod.WEEKLY) {
                payment.setLastExecutionTime(payment.getNextExecutionTime());
                payment.setNextExecutionTime(payment.getNextExecutionTime().plusWeeks(1));
            } else {
                payment.setLastExecutionTime(payment.getNextExecutionTime());
                payment.setNextExecutionTime(payment.getNextExecutionTime().plusMonths(1));
            }
            savePayment(payment);
        }
    }

    public void executePayment(RecurringPayment payment) {
        SendMoneyRequest request = buildSendMoneyRequest(payment);

        sendMoneyService.sendMoney(request);

        payment.setLastExecutionTime(LocalDate.now());
        payment.setNextExecutionTime(payment.getPeriod().getNextDate());

        recurringPaymentsRepository.save(payment);
    }

    public void savePayment(RecurringPayment payment) {
        recurringPaymentsRepository.save(payment);
    }

    private SendMoneyRequest buildSendMoneyRequest(RecurringPayment payment) {
        Account account = accountService.getById(payment.getAccountId());
        Wallet recipient = walletService.getWalletById(payment.getRecipientId());

        SendMoneyRequest request = new SendMoneyRequest();
        request.setEmail(payment.getRecipient());
        request.setUsername(userService.getUserByWalletId(payment.getWalletId()).getUsername());
        request.setAccountId(payment.getAccountId());
        request.setAmount(payment.getAmount());
        request.setCurrency(account.getCurrency());
        request.setWalletName(recipient.getName());
        return request;
    }

    public List<RecurringPaymentResponse> getRecurringPayments() {
        List<RecurringPaymentResponse> response = new ArrayList<>();
        List<RecurringPayment> payments = recurringPaymentsRepository
                .findAllByWalletId(walletService.getWallet().getId());
        for (int i = 0; i < payments.size(); i++) {
            RecurringPayment payment = payments.get(i);
            if (payment.getActive()) {
                RecurringPaymentResponse singleResponse =
                        new RecurringPaymentResponse(payment.getRecipient(),
                                payment.getAmount(),
                                payment.getLastExecutionTime(),
                                payment.getNextExecutionTime(),
                                payment.getPeriod());
                response.add(singleResponse);
            }
        }
        return response;
    }

    public List<RecurringPayment> findAllForExecution() {
        return recurringPaymentsRepository.findAllForExecution();
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
