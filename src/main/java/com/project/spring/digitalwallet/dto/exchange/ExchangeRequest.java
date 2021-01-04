package com.project.spring.digitalwallet.dto.exchange;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRequest {

    private Long walletId;
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;

}
