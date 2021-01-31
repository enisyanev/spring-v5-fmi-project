package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.instruments.PaymentInstrumentsDto;
import com.project.spring.digitalwallet.model.Bank;
import com.project.spring.digitalwallet.model.card.Card;
import com.project.spring.digitalwallet.service.PaymentInstrumentService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/payment-instruments")
public class PaymentInstrumentController {

    private PaymentInstrumentService paymentInstrumentService;

    public PaymentInstrumentController(PaymentInstrumentService paymentInstrumentService) {
        this.paymentInstrumentService = paymentInstrumentService;
    }

    @GetMapping
    public PaymentInstrumentsDto getPaymentInstruments() {
        return paymentInstrumentService.getPaymentInstruments();
    }

    @PostMapping("/cards")
    public Card addCard(@RequestBody @Valid Card card) {
        return paymentInstrumentService.createCard(card);
    }

    @PostMapping("/banks")
    public Bank addBank(@RequestBody @Valid Bank bank) {
        return paymentInstrumentService.createBank(bank);
    }

}
