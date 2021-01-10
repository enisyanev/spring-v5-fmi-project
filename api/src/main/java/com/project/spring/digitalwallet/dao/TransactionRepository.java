package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
