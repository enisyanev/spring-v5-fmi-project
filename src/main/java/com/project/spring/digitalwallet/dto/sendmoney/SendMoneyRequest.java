package com.project.spring.digitalwallet.dto.sendmoney;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendMoneyRequest {

    @NotNull
    private String email;
    @NotNull
    private String currency;
    @NotNull
    private String username;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Long accountId;

    private String walletName;

    private Long walletId;

}
