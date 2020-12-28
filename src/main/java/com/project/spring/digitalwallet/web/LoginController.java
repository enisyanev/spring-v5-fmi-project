package com.project.spring.digitalwallet.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.model.user.Credentials;
import com.project.spring.digitalwallet.model.user.User;
import com.project.spring.digitalwallet.service.UserService;
import com.project.spring.digitalwallet.utils.JwtUtils;

import javax.validation.Valid;


@RestController
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public String login(@Valid @RequestBody Credentials credentials, Errors errors) {
        if(errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid username or password");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.getUsername(), credentials.getPassword()));
        final User user = userService.getUserByUsername(credentials.getUsername());
        final String token = jwtUtils.generateToken(user);
        System.out.println(user.getAuthorities());
        log.info("Login successfull for {}: {}", user.getUsername(), token); //remove it!
        return token;
    }

    @PostMapping("/api/register")
    public User register(@Valid @RequestBody User user, Errors errors){
        return userService.addUser(user);
    }
}
