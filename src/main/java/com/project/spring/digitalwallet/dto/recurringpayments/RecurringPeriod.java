package com.project.spring.digitalwallet.dto.recurringpayments;

import java.time.LocalDate;

public enum RecurringPeriod {
    DAILY {
        @Override
        public LocalDate getNextDate() {
            return LocalDate.now().plusDays(1);
        }
    },
    WEEKLY {
        @Override
        public LocalDate getNextDate() {
            return LocalDate.now().plusWeeks(1);
        }
    },
    MONTHLY {
        @Override
        public LocalDate getNextDate() {
            return LocalDate.now().plusMonths(1);
        }
    };

    public abstract LocalDate getNextDate();
}
