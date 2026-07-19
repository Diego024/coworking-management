package com.company.coworking.management.service;

import com.company.coworking.management.security.dto.request.LoginRequest;
import com.company.coworking.management.security.dto.request.RegisterRequest;
import com.company.coworking.management.security.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);
}