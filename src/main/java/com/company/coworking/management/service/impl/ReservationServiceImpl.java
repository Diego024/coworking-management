package com.company.coworking.management.service.impl;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.dto.response.ReservationResponse;
import com.company.coworking.management.pipeline.reservation.ReservationPipeline;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.service.ReservationService;
import com.company.coworking.management.util.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationPipeline reservationPipeline;
    private final ReservationMapper reservationMapper;

    @Transactional
    @Override
    public ReservationResponse createReservation(CreateReservationRequest request) {
        log.info("Processing reservation creation request for user ID: {}", request.getUserId());

        CreateReservationContext context = new CreateReservationContext(request);
        reservationPipeline.execute(context);

        return reservationMapper.toResponse(context.getReservation());
    }
}
