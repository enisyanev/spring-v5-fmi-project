package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.CardRepository;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.card.Card;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card createCard(Card card) {
        card.setActive(true);
        return cardRepository.save(card);
    }

    public List<Card> getCardsByWalletId(long walletdId) {
        return cardRepository.findByWalletId(walletdId);
    }

    public Card getByIdAndWalletId(long id, long walletId) {
        return cardRepository.findByIdAndWalletId(id, walletId)
            .orElseThrow(() -> new NonexistingEntityException(
                String.format("Card with ID:%s for wallet %s does not exist.", id, walletId)));
    }

    public void deactivateCard(Card card) {
        card.setActive(false);
        cardRepository.save(card);
    }
}
