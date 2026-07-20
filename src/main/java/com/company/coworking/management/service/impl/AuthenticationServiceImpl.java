package com.company.coworking.management.service.impl;

import com.company.coworking.management.dto.request.LoginRequest;
import com.company.coworking.management.dto.request.RegisterRequest;
import com.company.coworking.management.dto.response.AuthenticationResponse;
import com.company.coworking.management.entity.User;
import com.company.coworking.management.exception.business.EmailAlreadyExistsException;
import com.company.coworking.management.repository.UserRepository;
import com.company.coworking.management.security.jwt.JwtProperties;
import com.company.coworking.management.security.jwt.JwtService;
import com.company.coworking.management.service.AuthenticationService;
import com.company.coworking.management.mapper.AuthenticationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.company.coworking.management.security.jwt.JwtAuthenticationFilter.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationMapper authenticationMapper;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return authenticationMapper.toAuthenticationResponse(
                user.getEmail(),
                TOKEN_PREFIX + token,
                jwtProperties.getExpiration()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + request.getEmail())
                );

        log.info("User '{}' authenticated successfully.", user.getEmail());

        String token = jwtService.generateToken(user);

        return authenticationMapper.toAuthenticationResponse(
                user.getEmail(),
                TOKEN_PREFIX + token,
                jwtProperties.getExpiration()
        );
    }
}