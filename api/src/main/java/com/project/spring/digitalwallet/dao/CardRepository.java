package com.project.spring.digitalwallet.dao;

import com.project.spring.digitalwallet.model.card.Card;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByWalletId(long walletId);

    Optional<Card> findByIdAndWalletId(long id, long walletId);
}
