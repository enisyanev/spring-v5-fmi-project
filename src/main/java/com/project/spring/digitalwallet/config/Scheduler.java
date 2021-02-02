package com.project.spring.digitalwallet.config;

import com.project.spring.digitalwallet.dto.recurringpayments.RecurringPeriod;
import com.project.spring.digitalwallet.model.recurring.RecurringPayment;
import com.project.spring.digitalwallet.service.RecurringPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class Scheduler {
    @Autowired
    private RecurringPaymentService recurringPaymentService;

    @Scheduled(cron = "30 36 23 1 2 ?")
    public void cronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        recurringPaymentService.executeRecurringPayments();
        System.out.println("Java cron job expression:: " + strDate);
    }
}
