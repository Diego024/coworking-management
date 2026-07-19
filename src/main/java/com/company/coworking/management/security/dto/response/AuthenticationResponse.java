package com.company.coworking.management.security.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String user;
    private String accessToken;
    private Long expiresIn;
}