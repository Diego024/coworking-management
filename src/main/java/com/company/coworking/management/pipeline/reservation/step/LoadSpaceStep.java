package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.entity.Space;
import com.company.coworking.management.exception.business.SpaceNotFoundException;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadSpaceStep implements ReservationStep {

    private final SpaceRepository spaceRepository;

    @Override
    public void execute(CreateReservationContext context) {
        Space space = spaceRepository.findById(context.getRequest().getSpaceId())
                .orElseThrow(() -> new SpaceNotFoundException(context.getRequest().getSpaceId()));

        context.setSpace(space);
    }
}