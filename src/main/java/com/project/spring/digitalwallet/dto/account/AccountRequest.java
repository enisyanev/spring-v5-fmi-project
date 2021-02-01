package com.project.spring.digitalwallet.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest {
    @NotNull
    private String username;
    @NotNull
    private String currency;
}
