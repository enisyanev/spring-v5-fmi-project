package com.project.spring.digitalwallet.dto.exchange;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRequest {

    @NotNull
    private Long walletId;

    @NotNull
    private Long fromAccount;

    @NotNull
    private Long toAccount;

    @NotNull
    private BigDecimal amount;

}
