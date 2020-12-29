package com.project.spring.digitalwallet.model.transaction;

import java.math.BigDecimal;
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
    private String walletName;
    private Long slipId;
    private String currency;
    private BigDecimal amount;

    public ScheduledTransaction() {
        // non-args constructor
    }

    public ScheduledTransaction(String walletName, Long slipId, String currency,
                                BigDecimal amount) {
        this.walletName = walletName;
        this.slipId = slipId;
        this.currency = currency;
        this.amount = amount;
    }

}
