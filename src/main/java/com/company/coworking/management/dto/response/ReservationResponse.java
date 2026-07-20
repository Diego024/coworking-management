package com.company.coworking.management.dto.response;

import com.company.coworking.management.util.enums.ReservationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;

    private String user;

    private String spaceName;

    private BigDecimal totalPrice;

    private ReservationStatus status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}