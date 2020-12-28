package com.project.spring.digitalwallet.dto.sendmoney;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SendMoneyRequest {

    // TODO: Or other identificator of the recipient
    private String walletName;
    private String currency;
    private BigDecimal amount;
    private Long accountId;
}
