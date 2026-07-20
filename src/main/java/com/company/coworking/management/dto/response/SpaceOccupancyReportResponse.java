package com.company.coworking.management.dto.response;

import com.company.coworking.management.enums.SpaceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpaceOccupancyReportResponse {

    private Long spaceId;
    private String spaceName;
    private SpaceType spaceType;
    private BigDecimal hourlyRate;
    private BigDecimal occupancyPercentage;

}