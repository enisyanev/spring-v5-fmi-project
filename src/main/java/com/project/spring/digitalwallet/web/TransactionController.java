package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.transaction.TransactionDto;
import com.project.spring.digitalwallet.service.TransactionService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/csv")
    public void downloadTransactionsHistoryAsCsv(HttpServletResponse response) throws IOException {
        transactionService.downloadTransactionsHistoryAsCsv(response);
    }

}
