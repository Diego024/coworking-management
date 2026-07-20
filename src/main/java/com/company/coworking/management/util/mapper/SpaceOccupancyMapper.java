package com.company.coworking.management.util.mapper;

import com.company.coworking.management.common.mapper.GlobalMapperConfig;
import com.company.coworking.management.dto.response.SpaceOccupancyReportResponse;
import com.company.coworking.management.entity.Space;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(config = GlobalMapperConfig.class)
public interface SpaceOccupancyMapper {

    @Mapping(source = "space.id", target = "spaceId")
    @Mapping(source = "space.name", target = "spaceName")
    @Mapping(source = "space.type", target = "spaceType")
    SpaceOccupancyReportResponse toResponse(Space space, BigDecimal occupancyPercentage);
}
