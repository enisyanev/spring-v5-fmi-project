package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.Bank;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findByWalletId(long walletId);
}
