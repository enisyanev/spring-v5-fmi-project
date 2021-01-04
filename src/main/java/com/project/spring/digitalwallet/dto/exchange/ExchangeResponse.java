package com.project.spring.digitalwallet.dto.exchange;

import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponse {

    private Long fromAccount;
    private Long toAccount;
    private TransactionStatus transactionStatus;
    private Long slipId;

}
