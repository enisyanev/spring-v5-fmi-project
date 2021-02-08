package com.project.spring.digitalwallet.dto.transaction;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionsHistoryPagedDto {
    private List<TransactionDto> transactions;
    private int totalPages;
    private int pageNumber;
    private int pageSize;

    public TransactionsHistoryPagedDto(List<TransactionDto> transactions, int totalPages, int pageNumber,
            int pageSize) {
        this.transactions = transactions;
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
}
