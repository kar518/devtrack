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

    if (user.getUsername() == null || user.getPassword() == null) {
        return "Invalid input";
    }

    User existing = repo.findByUsername(user.getUsername());

    if (existing == null) {
        return "User not found";
    }

    if (existing.getPassword() == null) {
        return "User data corrupted";
    }

    if (!existing.getPassword().equals(user.getPassword())) {
        return "Invalid password";
    }

    return "TOKEN_" + existing.getUsername();
}
}
