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

    // TODO: Or other identificator of the recipient
    @NotNull
    private String walletName;
    @NotNull
    private String currency;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private Long accountId;

    private Long walletId;
}
