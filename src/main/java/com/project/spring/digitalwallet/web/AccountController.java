package com.project.spring.digitalwallet.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.group.Group;
import com.project.spring.digitalwallet.service.AccountService;
import com.project.spring.digitalwallet.service.GroupService;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	
	@Autowired
    private AccountService accountService;

	@GetMapping
	public List<Account> getAllGroups(@RequestParam String username){
		return accountService.getByWalletIdUsingUsername(username);
	}

}
