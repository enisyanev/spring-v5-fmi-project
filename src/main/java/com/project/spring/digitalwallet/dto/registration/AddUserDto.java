package com.project.spring.digitalwallet.dto.registration;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.project.spring.digitalwallet.utils.UserPermission;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddUserDto {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private List<UserPermission> permissions;

}
