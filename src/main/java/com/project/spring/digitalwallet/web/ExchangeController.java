package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.exchange.ExchangeRequest;
import com.project.spring.digitalwallet.dto.exchange.ExchangeResponse;
import com.project.spring.digitalwallet.service.ExchangeService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping
    public ExchangeResponse exchange(@Valid @RequestBody ExchangeRequest request) {
        return exchangeService.exchange(request);
    }

}
