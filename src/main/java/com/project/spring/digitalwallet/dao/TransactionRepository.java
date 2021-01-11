package com.project.spring.digitalwallet.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.spring.digitalwallet.model.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByWalletId(long walletId, Pageable pageable);

    List<Transaction> findByWalletId(long walletId);
}
