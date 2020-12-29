package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.transaction.ScheduledTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledTransactionRepository extends JpaRepository<ScheduledTransaction, Long> {
}
