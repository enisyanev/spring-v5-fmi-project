package com.project.spring.digitalwallet.dto.recurringpayments;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRecurringPaymentRequest {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private RecurringPeriod period;

}
