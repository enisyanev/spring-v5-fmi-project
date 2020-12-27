package com.project.spring.digitalwallet.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.digitalwallet.model.User;
import com.project.spring.digitalwallet.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
    private UserService userService;
	
	@GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
