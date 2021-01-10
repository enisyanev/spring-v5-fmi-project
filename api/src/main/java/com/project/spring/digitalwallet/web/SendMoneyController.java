package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyRequest;
import com.project.spring.digitalwallet.dto.sendmoney.SendMoneyResponse;
import com.project.spring.digitalwallet.service.SendMoneyService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/send-money")
public class SendMoneyController {

    private SendMoneyService sendMoneyService;

    public SendMoneyController(SendMoneyService sendMoneyService) {
        this.sendMoneyService = sendMoneyService;
    }

    @PostMapping
    public SendMoneyResponse createSendMoney(@Valid @RequestBody SendMoneyRequest request) {
        return sendMoneyService.sendMoney(request);
    }

}
