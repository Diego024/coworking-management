package com.company.coworking.management.mapper;

import com.company.coworking.management.common.mapper.GlobalMapperConfig;
import com.company.coworking.management.security.dto.response.AuthenticationResponse;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface AuthenticationMapper {

    AuthenticationResponse toAuthenticationResponse(String user, String accessToken, Long expiresIn);
}