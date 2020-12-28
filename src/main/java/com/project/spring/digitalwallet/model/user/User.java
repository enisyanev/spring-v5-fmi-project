package com.project.spring.digitalwallet.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name="User")
@Getter
@Setter
@AllArgsConstructor
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;


    public User(String firstName,String lastName,String username,String email,String password) {
    	this.email=email;
    	this.firstname=firstName;
    	this.lastname=lastName;
    	this.username=username;
    	this.password=password;
    	this.role= Role.USER;
    }

	public User() {
		
	}
	private Role role;
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		List<Role> roles=new ArrayList<>();
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
