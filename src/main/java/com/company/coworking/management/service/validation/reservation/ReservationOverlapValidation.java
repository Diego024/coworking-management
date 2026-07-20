package com.company.coworking.management.service.validation.reservation;


import com.company.coworking.management.exception.business.SpaceAlreadyReservedException;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(ReservationValidationOrder.OVERLAPPING)
@RequiredArgsConstructor
public class ReservationOverlapValidation implements ReservationValidation {

    private final ReservationRepository reservationRepository;

    @Override
    public void validate(CreateReservationContext context) {
        Long spaceId = context.getRequest().getSpaceId();
        boolean isOverlapping = reservationRepository.existsOverlappingReservation(
                spaceId,
                context.getRequest().getStartTime(),
                context.getRequest().getEndTime()
        );

        if (isOverlapping) {
            throw new SpaceAlreadyReservedException("The space is already reserved for the requested time period.");
        }
    }
}