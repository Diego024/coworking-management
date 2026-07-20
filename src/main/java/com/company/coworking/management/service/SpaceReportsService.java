package com.company.coworking.management.service;

import com.company.coworking.management.dto.response.SpaceOccupancyReportResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface SpaceReportsService {
    List<SpaceOccupancyReportResponse> getSpaceOccupancy(LocalDateTime startDate, LocalDateTime endDate);
}
