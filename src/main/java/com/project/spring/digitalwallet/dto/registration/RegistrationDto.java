package com.project.spring.digitalwallet.dto.registration;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationDto {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String currency;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

}
