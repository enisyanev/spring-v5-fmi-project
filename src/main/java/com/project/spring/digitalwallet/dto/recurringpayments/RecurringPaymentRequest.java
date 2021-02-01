package com.project.spring.digitalwallet.dto.recurringpayments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RecurringPaymentRequest {

    @NotNull
    private Long accountId;
    @NotBlank
    private String email;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private RecurringPeriod period;

}
