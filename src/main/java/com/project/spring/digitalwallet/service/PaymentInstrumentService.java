package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dto.instruments.PaymentInstrumentsDto;
import com.project.spring.digitalwallet.model.Bank;
import com.project.spring.digitalwallet.model.card.Card;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaymentInstrumentService {

    private WalletService walletService;
    private CardService cardService;
    private BankService bankService;

    public PaymentInstrumentService(WalletService walletService,
                                    CardService cardService,
                                    BankService bankService) {
        this.walletService = walletService;
        this.cardService = cardService;
        this.bankService = bankService;
    }

    public PaymentInstrumentsDto getPaymentInstruments() {
        long walletId = walletService.getWallet().getId();
        List<Card> cards = cardService.getCardsByWalletId(walletId);
        List<Bank> banks = bankService.getBankByWalletId(walletId);

        return new PaymentInstrumentsDto(cards, banks);
    }

    public Card createCard(Card card) {
        long walletId = walletService.getWallet().getId();
        card.setWalletId(walletId);
        return cardService.createCard(card);
    }

    public Bank createBank(Bank bank) {
        long walletId = walletService.getWallet().getId();
        bank.setWalletId(walletId);
        return bankService.createBank(bank);
    }

}
