package com.company.coworking.management.util.mapper;

import com.company.coworking.management.common.mapper.GlobalMapperConfig;
import com.company.coworking.management.dto.response.ReservationResponse;
import com.company.coworking.management.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface ReservationMapper {

    @Mapping(source = "user.email", target = "user")
    @Mapping(source = "space.name", target = "spaceName")
    ReservationResponse toResponse(Reservation reservation);

}
