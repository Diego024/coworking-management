package com.company.coworking.management.mapper;

import com.company.coworking.management.common.mapper.GlobalMapperConfig;
import com.company.coworking.management.dto.request.CreateSpaceRequest;
import com.company.coworking.management.dto.request.EditSpaceRequest;
import com.company.coworking.management.dto.response.SpaceResponse;
import com.company.coworking.management.entity.Space;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = {ReservationMapper.class})
public interface SpaceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "spaceType", target = "type")
    @Mapping(target = "capacity", source = "capacity")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "reservations", ignore = true)
    Space toEntity(CreateSpaceRequest createSpaceRequest);

    @Mapping(source = "type", target = "spaceType")
    SpaceResponse toResponse(Space space);

    List<SpaceResponse> toResponseList(List<Space> spaces);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "spaceType", target = "type")
    @Mapping(target = "capacity", source = "capacity")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "reservations", ignore = true)
    void updateEntityFromRequest(EditSpaceRequest request, @MappingTarget Space space);
}
