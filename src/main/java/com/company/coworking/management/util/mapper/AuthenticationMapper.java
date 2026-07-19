package com.company.coworking.management.util.mapper;

import com.company.coworking.management.common.mapper.GlobalMapperConfig;
import com.company.coworking.management.dto.response.AuthenticationResponse;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface AuthenticationMapper {

    AuthenticationResponse toAuthenticationResponse(String user, String accessToken, Long expiresIn);
}