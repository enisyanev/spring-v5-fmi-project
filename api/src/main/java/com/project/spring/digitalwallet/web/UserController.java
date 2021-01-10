package com.project.spring.digitalwallet.web;

import com.project.spring.digitalwallet.model.user.User;
import com.project.spring.digitalwallet.service.UserService;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        User created = userService.addUser(user);
        return created;
    }

    @PutMapping
    public void updateUser(@Valid @RequestBody User user) {
        userService.updateUser(user);
    }

    @DeleteMapping
    public User deleteUser(@RequestBody Map<String, String> usermap) {
        return userService.deleteUser(usermap.get("username"));
    }

}
