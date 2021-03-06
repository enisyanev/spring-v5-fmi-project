package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentRequest;
import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPaymentResponse;
import com.project.spring.digitalwallet.dto.recurringpayments.UpdateRecurringPaymentRequest;
import com.project.spring.digitalwallet.model.recurring.RecurringPayment;
import com.project.spring.digitalwallet.service.RecurringPaymentService;

import java.util.List;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/recurring-payments")
public class RecurringPaymentController {

    private RecurringPaymentService recurringPaymentService;

    public RecurringPaymentController(RecurringPaymentService recurringPaymentService) {
        this.recurringPaymentService = recurringPaymentService;
    }

    @GetMapping
    public List<RecurringPaymentResponse> getRecurringPayments() {
        return recurringPaymentService.getRecurringPayments();
    }

    @DeleteMapping("/{id}")
    public void deleteReccuring(@PathVariable("id") Long recurringPaymentId) {
        recurringPaymentService.deleteRecurringPayment(recurringPaymentId);
    }

    @PostMapping
    public void createRecurringPayment(@RequestBody @Valid RecurringPaymentRequest request) {
        recurringPaymentService.createRecurringPayment(request);
    }

    @PutMapping("/{id}")
    public void updateRecurringPayment(@PathVariable("id") Long recurringPaymentId,
                                       @RequestBody UpdateRecurringPaymentRequest request) {
        recurringPaymentService.updateRecurringPayment(recurringPaymentId, request);
    }

    // TODO: add a Quartz job or similar and execute this on some period
    @PostMapping("/execute")
    public void executeRecurringPayments() {
        recurringPaymentService.executeRecurringPayments();
    }

}
