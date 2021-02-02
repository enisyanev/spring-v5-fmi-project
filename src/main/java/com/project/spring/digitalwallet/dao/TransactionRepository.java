package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.transaction.Transaction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByWalletId(long walletId, Pageable pageable);

    List<Transaction> findByWalletId(long walletId);

    Optional<Transaction> findFirstBySlipId(Long slipId);
}
