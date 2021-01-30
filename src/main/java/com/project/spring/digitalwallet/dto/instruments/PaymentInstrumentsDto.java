package com.project.spring.digitalwallet.dto.instruments;

import com.project.spring.digitalwallet.model.Bank;
import com.project.spring.digitalwallet.model.card.Card;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentInstrumentsDto {

    private List<Card> cards;
    private List<Bank> banks;

}
