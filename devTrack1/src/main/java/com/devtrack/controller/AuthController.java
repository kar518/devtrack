package com.devtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.devtrack.model.User;
import com.devtrack.service.AuthService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService service;

    // REGISTER
    @PostMapping("/api/register")
    public String register(@RequestBody User user) {
        return service.register(user);
    }

    // LOGIN
    @PostMapping("/api/login")
    public String login(@RequestBody User user) {
        return service.login(user);
    }
}