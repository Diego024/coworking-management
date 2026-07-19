package com.company.coworking.management.service;

import com.company.coworking.management.dto.request.LoginRequest;
import com.company.coworking.management.dto.request.RegisterRequest;
import com.company.coworking.management.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);
}