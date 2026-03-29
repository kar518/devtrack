package com.devtrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devtrack.model.User;
import com.devtrack.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    // REGISTER
    public String register(User user) {
        repo.save(user);
        return "User registered successfully";
    }

    // LOGIN
    public String login(User user) {

        User existing = repo.findByUsername(user.getUsername());

        if (existing != null && existing.getPassword().equals(user.getPassword())) {
            return "TOKEN_" + user.getUsername(); // simple token
        }

        return "Invalid username or password";
    }
}