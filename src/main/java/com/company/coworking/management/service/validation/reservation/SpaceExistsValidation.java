package com.company.coworking.management.service.validation.reservation;

import com.company.coworking.management.exception.business.SpaceNotFoundException;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(ReservationValidationOrder.SPACE_EXISTS)
@RequiredArgsConstructor
public class SpaceExistsValidation implements ReservationValidation {

    private final SpaceRepository spaceRepository;

    @Override
    public void validate(CreateReservationContext context) {
        spaceRepository.findById(context.getSpace().getId())
                .orElseThrow(() -> new SpaceNotFoundException(context.getSpace().getId()));
    }
}