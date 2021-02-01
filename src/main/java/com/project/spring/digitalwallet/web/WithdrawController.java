package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.upload.MoneyRequest;
import com.project.spring.digitalwallet.dto.upload.MoneyResponse;
import com.project.spring.digitalwallet.service.WithdrawService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/withdraw")
public class WithdrawController {

    private WithdrawService withdrawService;

    public WithdrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @PostMapping
    public MoneyResponse withdraw(@RequestBody @Valid MoneyRequest request) {
        return this.withdrawService.withdraw(request);
    }

}
