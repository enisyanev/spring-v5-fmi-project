package com.project.spring.digitalwallet.dto.sendmoney;

import com.project.spring.digitalwallet.model.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMoneyResponse {

    private Long slipId;
    private Long walletId;
    private Long accountId;
    private TransactionStatus status;

}
