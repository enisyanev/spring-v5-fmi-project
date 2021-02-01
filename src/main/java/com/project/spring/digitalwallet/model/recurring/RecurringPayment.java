package com.project.spring.digitalwallet.model.recurring;

import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPeriod;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RECURRING_PAYMENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "WALLET_ID")
    private Long walletId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "RECIPIENT_ID")
    private Long recipientId;

    @Column(name = "RECIPIENT")
    private String recipient;

    @Column(name = "LAST_EXECUTION_TIME")
    private LocalDate lastExecutionTime;

    @Column(name = "NEXT_EXECUTION_TIME")
    private LocalDate nextExecutionTime;

    @Column(name = "PERIOD")
    @Enumerated(EnumType.STRING)
    private RecurringPeriod period;

    @Column(name = "ACTIVE")
    private Boolean active;
}
