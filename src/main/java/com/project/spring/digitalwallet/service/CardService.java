package com.project.spring.digitalwallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.spring.digitalwallet.dao.CardRepository;
import com.project.spring.digitalwallet.dao.WalletRepository;
import com.project.spring.digitalwallet.model.card.Card;

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
	
	public void deactivateCard(Card card) {
		card.setActive(false);
        cardRepository.save(card);
	}
}
