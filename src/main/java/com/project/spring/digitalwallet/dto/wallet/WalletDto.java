package com.project.spring.digitalwallet.dto.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.AccountStatus;
import com.project.spring.digitalwallet.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {

    private Wallet wallet;
    private List<Account> accounts;

    @JsonIgnore
    public Account getDefaultAccount() {
        return accounts.stream().filter(x -> x.getStatus() == AccountStatus.PRIMARY).findFirst().get();
    }

}
