package com.project.spring.digitalwallet.model.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @NotNull
    @Size(min = 2, max = 30)
    private String username;
    @NotNull
    @Size(min = 4, max = 30)
    private String password;
}
