package com.company.coworking.management.dto.response;

import com.company.coworking.management.util.enums.SpaceType;
import lombok.Data;

import java.util.List;

@Data
public class SpaceResponse {
    private Long id;
    private String name;
    private SpaceType spaceType;
    private Integer capacity;
    private String location;
    private List<ReservationResponse> reservations;
}
