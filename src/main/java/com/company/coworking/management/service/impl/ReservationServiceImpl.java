package com.company.coworking.management.service.impl;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.dto.response.ReservationResponse;
import com.company.coworking.management.entity.Reservation;
import com.company.coworking.management.entity.User;
import com.company.coworking.management.exception.business.ReservationCannotBeCancelledException;
import com.company.coworking.management.exception.business.ReservationNotFoundException;
import com.company.coworking.management.pipeline.reservation.ReservationPipeline;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.repository.ReservationRepository;
import com.company.coworking.management.service.CurrentUserService;
import com.company.coworking.management.service.ReservationService;
import com.company.coworking.management.enums.ReservationStatus;
import com.company.coworking.management.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private static final Set<ReservationStatus> CANCELLABLE_STATUSES = Set.of(
            ReservationStatus.CONFIRMED,
            ReservationStatus.PENDING_PAYMENT
    );

    private final ReservationPipeline reservationPipeline;
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    @CacheEvict(
            value = "space-occupancy",
            allEntries = true
    )
    public ReservationResponse createReservation(CreateReservationRequest request) {

        CreateReservationContext context = new CreateReservationContext(request);
        reservationPipeline.execute(context);

        return reservationMapper.toResponse(context.getReservation());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getCurrentUserReservations() {
        User currentUser = currentUserService.getCurrentUser();

        return reservationRepository.findByUserId(currentUser.getId())
                .stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservations() {
        return reservationMapper.toResponseList(reservationRepository.findAllOrderByCreatedAtDesc());
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "space-occupancy",
            allEntries = true
    )
    public ReservationResponse cancelReservation(Long reservationId) {
        Reservation reservation;
        if (currentUserService.isAdmin()) {
            reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() ->
                            new ReservationNotFoundException(reservationId)
                    );

        } else {
            reservation = reservationRepository.findByIdAndUserId(reservationId, currentUserService.getCurrentUserId())
                    .orElseThrow(() ->
                            new ReservationNotFoundException(reservationId)
                    );
        }

        if (!CANCELLABLE_STATUSES.contains(reservation.getStatus())) {
            throw new ReservationCannotBeCancelledException(reservation.getId());
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation updatedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponse(updatedReservation);
    }
}
