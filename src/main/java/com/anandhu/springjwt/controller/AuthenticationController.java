package com.anandhu.springjwt.controller;

import com.anandhu.springjwt.model.User;
import com.anandhu.springjwt.service.AuthenticationResponse;
import com.anandhu.springjwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User requets){
        return ResponseEntity.ok(authenticationService.register(requets));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user){
        return ResponseEntity.ok(authenticationService.authenticate(user));

    }
}
