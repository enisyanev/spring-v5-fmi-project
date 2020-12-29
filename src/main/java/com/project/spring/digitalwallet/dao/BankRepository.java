package com.project.spring.digitalwallet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.spring.digitalwallet.model.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findByWalletId(long walletId);
}
