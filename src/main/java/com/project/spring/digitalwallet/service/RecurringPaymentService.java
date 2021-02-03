package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.RecurringPaymentsRepository;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentResponse;
import com.project.spring.digitalwallet.dto.recurringpayments.UpdateRecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyRequest;
import com.project.spring.digitalwallet.dto.wallet.WalletDto;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.Wallet;
import com.project.spring.digitalwallet.model.recurring.RecurringPayment;
import com.project.spring.digitalwallet.model.user.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    public void deleteRecurringPayment(Long id) {
        recurringPaymentsRepository.deleteById(id);
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
        List<RecurringPayment> forExecution = findAllForExecution();
        forExecution.forEach(this::executePayment);
    }

    public void executePayment(RecurringPayment payment) {
        try {
            SendMoneyRequest request = buildSendMoneyRequest(payment);

            sendMoneyService.sendMoney(request);

            payment.setLastExecutionTime(LocalDate.now());
            payment.setNextExecutionTime(payment.getPeriod().getNextDate());

            recurringPaymentsRepository.save(payment);
        } catch (Exception e) {
            log.error("Error while processing recurring payment: {}", payment.getId());
        }
    }

    private SendMoneyRequest buildSendMoneyRequest(RecurringPayment payment) {
        Account account = accountService.getById(payment.getAccountId());

        SendMoneyRequest request = new SendMoneyRequest();
        request.setEmail(payment.getRecipient());
        request.setUsername(userService.getUserByWalletId(payment.getWalletId()).getUsername());
        request.setAccountId(payment.getAccountId());
        request.setAmount(payment.getAmount());
        request.setCurrency(account.getCurrency());
        return request;
    }

    public List<RecurringPaymentResponse> getRecurringPayments() {
        List<RecurringPayment> payments = recurringPaymentsRepository
            .findAllByWalletId(walletService.getWallet().getId());
        return payments.stream()
            .filter(RecurringPayment::getActive)
            .map(payment -> new RecurringPaymentResponse(payment.getId(),
                payment.getRecipient(),
                payment.getAmount(),
                payment.getLastExecutionTime(),
                payment.getNextExecutionTime(),
                payment.getPeriod()))
            .collect(Collectors.toList());
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

        if (!request.getAmount().equals(BigDecimal.valueOf(0))) {
            payment.setAmount(request.getAmount());
        }
        payment.setPeriod(request.getPeriod());

        recurringPaymentsRepository.save(payment);
    }
}
