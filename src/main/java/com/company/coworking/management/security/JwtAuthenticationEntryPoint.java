package com.company.coworking.management.security;


import com.company.coworking.management.dto.response.GeneralResponseWithErrors;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        GeneralResponseWithErrors body = new GeneralResponseWithErrors();

        body.setUri(request.getRequestURI());
        body.setStatus(HttpStatus.UNAUTHORIZED.value());
        body.setTimestamp(LocalDateTime.now(ZoneId.systemDefault()));
        body.setErrors(List.of("Authentication is required to access this resource."));

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}