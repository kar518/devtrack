package com.devtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.devtrack.model.User;
import com.devtrack.service.AuthService;

@RestController
@RequestMapping("/api")   // 🔥 add this for consistency
@CrossOrigin(origins = "*")  // 🔥 allow Vercel
public class AuthController {

    @Autowired
    private AuthService service;

    // REGISTER
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return service.register(user);
    }

    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return service.login(user);
    }
}
