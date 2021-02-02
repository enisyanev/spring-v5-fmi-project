package com.project.spring.digitalwallet.dto.recurringpayments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringPaymentResponse {
    @NotBlank
    private long id;
    @NotBlank
    private String receiver;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDate lastDate;
    @NotNull
    @FutureOrPresent
    private LocalDate nextDate;
    @Enumerated(EnumType.STRING)
    private RecurringPeriod period;
}
