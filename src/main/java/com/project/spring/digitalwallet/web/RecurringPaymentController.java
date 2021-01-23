package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentRequest;
import com.project.spring.digitalwallet.service.RecurringPaymentService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recurring-payment")
public class RecurringPaymentController {

    private RecurringPaymentService recurringPaymentService;

    public RecurringPaymentController(RecurringPaymentService recurringPaymentService) {
        this.recurringPaymentService = recurringPaymentService;
    }

    @PostMapping
    public void createRecurringPayment(@RequestBody @Valid RecurringPaymentRequest request) {
        recurringPaymentService.createRecurringPayment(request);
    }

    // TODO: Add update recurring method

    // TODO: add a Quartz job or similar and execute this on some period
    @PostMapping("/execute")
    public void executeRecurringPayments() {
        recurringPaymentService.executeRecurringPayments();
    }

}
