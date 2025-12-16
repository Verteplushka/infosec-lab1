package com.InformationSecurity.Lab1.controller;

import com.InformationSecurity.Lab1.repository.UserRepository;
import com.InformationSecurity.Lab1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {
    private final UserService userService;

    @Autowired
    public DataController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/data")
    public List<String> getData() {
        return userService.getAllUsers();
    }

    @GetMapping("/hello")
    public String hello(Authentication auth) {
        return "Hello, " + auth.getName();
    }
}

