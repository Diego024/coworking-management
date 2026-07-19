package com.company.coworking.management.controller;

import com.company.coworking.management.security.dto.request.LoginRequest;
import com.company.coworking.management.security.dto.request.RegisterRequest;
import com.company.coworking.management.security.dto.response.AuthenticationResponse;
import com.company.coworking.management.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.register(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.login(request);
        return ResponseEntity.ok(authenticationResponse);
    }
}