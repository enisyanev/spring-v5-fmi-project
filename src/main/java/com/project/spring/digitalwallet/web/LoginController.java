package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.dto.registration.AddUserDto;
import com.project.spring.digitalwallet.dto.registration.RegistrationDto;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.model.user.Credentials;
import com.project.spring.digitalwallet.model.user.User;
import com.project.spring.digitalwallet.service.RegistrationService;
import com.project.spring.digitalwallet.service.UserService;
import com.project.spring.digitalwallet.utils.JwtUtils;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/api/login")
    public Map<String, Object> login(@Valid @RequestBody Credentials credentials, Errors errors) {
        if (errors.hasErrors()) {
            throw new InvalidEntityDataException("Invalid username or password");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            credentials.getUsername(), credentials.getPassword()));
        final User user = userService.getUserByUsername(credentials.getUsername());
        final String token = jwtUtils.generateToken(user);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    @PostMapping("/api/register")
    public User register(@Valid @RequestBody RegistrationDto request) {
        return registrationService.register(request);
    }
    
    @PostMapping("/api/register/user")
    public User addUser(@Valid @RequestBody AddUserDto request) {
        return registrationService.addUser(request);
    }
}
