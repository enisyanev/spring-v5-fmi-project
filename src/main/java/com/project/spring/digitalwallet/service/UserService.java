package com.project.spring.digitalwallet.service;

import com.project.spring.digitalwallet.dao.UserRepository;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.exception.NonexistingEntityException;
import com.project.spring.digitalwallet.model.user.Role;
import com.project.spring.digitalwallet.model.user.User;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
                new NonexistingEntityException(String.format("User with ID:%s does not exist.", id)));
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() ->
                new InvalidEntityDataException("Invalid username or password."));
    }

    public User addUser(User user) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepo.save(user);
    }

    public User updateUser(User user) {
        userRepo.findByUsername(user.getUsername()).orElseThrow(() ->
                new InvalidEntityDataException("No user with that name"));
        return userRepo.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepo.findByUsername(email).orElseThrow(() ->
                new InvalidEntityDataException("Invalid username or password."));
    }
    
    @Transactional
    public User deleteUser(String username) {
        User removed = getUserByUsername(username);
        userRepo.deleteByUsername(username);
        return removed;
    }

    public boolean exist(String username) {
        return userRepo.countByUsername(username) > 0;
    }

    public List<User> getByWalletId(long walletId) {
        return userRepo.findByWalletId(walletId);
    }

    public List<User> getUsersByWalletId() {
        User logged = getLoggedUser();
        List<User> users = getByWalletId(logged.getId());
        long userId = logged.getId();
        users.removeIf(x -> x.getId() == userId);
        return users;
    }

    private User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserByUsername(authentication.getName());
    }
}
