package com.project.spring.digitalwallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.spring.digitalwallet.model.card.Card;
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findByWalletId(long walletId);
}
