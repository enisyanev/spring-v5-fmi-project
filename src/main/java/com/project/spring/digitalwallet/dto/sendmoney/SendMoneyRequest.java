package com.project.spring.digitalwallet.dto.sendmoney;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SendMoneyRequest {

    // TODO: Or other identificator of the recipient
    @NotNull
    private String walletName;
    @NotNull
    private String currency;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Long accountId;
}
