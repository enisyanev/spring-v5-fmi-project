package com.project.spring.digitalwallet.model.transaction;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SCHEDULED_TRNS")
@Getter
@Setter
public class ScheduledTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "SLIP_ID")
    private Long slipId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    public ScheduledTransaction() {
        // non-args constructor
    }

    public ScheduledTransaction(String email, Long slipId, String currency,
                                BigDecimal amount) {
        this.email = email;
        this.slipId = slipId;
        this.currency = currency;
        this.amount = amount;
    }

}
