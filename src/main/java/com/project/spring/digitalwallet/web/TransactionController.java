package com.project.spring.digitalwallet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.digitalwallet.dto.transaction.TransactionDto;
import com.project.spring.digitalwallet.service.TransactionService;

@RestController
@RequestMapping("/api/transactions-history")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<TransactionDto> getTransactionsHistory(@RequestParam(name = "pageNo") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        return transactionService.getTransactionsHistory(pageNo, pageSize);
    }

}
