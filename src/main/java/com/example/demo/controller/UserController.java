package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.RegisterRequest;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping({ "/user" })
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody RegisterRequest request) {

        userService.createUser(request.email(), request.name(), request.password());

        return ResponseEntity.ok("User created or recreated");
    }

    @GetMapping("/secured")
    public ResponseEntity<String> getSecured() {
        return ResponseEntity.ok("Secured method response");
    }

}
