package com.project.spring.digitalwallet.dto.recurringpayments;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecurringPaymentRequest {

    @NotNull
    private Long accountId;

    // TODO: Or other identificator of the recipient
    @NotBlank
    private String walletName;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private RecurringPeriod period;
    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

}
