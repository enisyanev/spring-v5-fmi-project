package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllGroups(@RequestParam String username) {
        return accountService.getByWalletIdUsingUsername(username);
    }

}
