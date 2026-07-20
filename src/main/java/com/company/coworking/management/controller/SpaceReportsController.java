package com.company.coworking.management.controller;

import com.company.coworking.management.dto.response.GeneralResponse;
import com.company.coworking.management.dto.response.ResponseBuilder;
import com.company.coworking.management.dto.response.SpaceOccupancyReportResponse;
import com.company.coworking.management.service.SpaceReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reports/spaces")
@RequiredArgsConstructor
@Tag(name = "Space Reports")
public class SpaceReportsController {

    private final SpaceReportsService spaceReportsService;

    @GetMapping("/occupancy")
    @Operation(summary = "Space occupancy report")
    public ResponseEntity<GeneralResponse> getSpaceOccupancy(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        List<SpaceOccupancyReportResponse> spaceOccupancy = spaceReportsService.getSpaceOccupancy(startDate, endDate);
        return ResponseBuilder.buildSuccessResponse(spaceOccupancy);
    }
}
