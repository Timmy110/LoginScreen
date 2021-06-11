package com.example.LoginScreen.controller;

import com.example.LoginScreen.user.User;
import com.example.LoginScreen.user.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/Users")
    List<User> all() {
        List<User> response = new ArrayList<User>();
        repository.findAll().iterator().forEachRemaining(response::add);
        return response;
    }
}
