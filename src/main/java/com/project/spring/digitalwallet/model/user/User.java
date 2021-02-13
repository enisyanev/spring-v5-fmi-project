package com.project.spring.digitalwallet.model.user;

import com.project.spring.digitalwallet.dto.registration.AddUserDto;
import com.project.spring.digitalwallet.dto.registration.RegistrationDto;
import com.project.spring.digitalwallet.utils.UserPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    
    @ElementCollection
    private List<UserPermission> permissions;

    private long walletId;

    private Role role;

    public User(RegistrationDto registrationDto, long walletId) {
        this.username = registrationDto.getUsername();
        this.password = registrationDto.getPassword();
        this.firstname = registrationDto.getFirstName();
        this.lastname = registrationDto.getLastName();
        this.walletId = walletId;
        this.permissions = UserPermission.getAllPermissions();
    }

    public User(AddUserDto addUserDto, long walletId) {
        this.username = addUserDto.getUsername();
        this.password = addUserDto.getPassword();
        this.firstname = addUserDto.getFirstName();
        this.lastname = addUserDto.getLastName();
        this.walletId = walletId;
        this.permissions = addUserDto.getPermissions();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString()))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
