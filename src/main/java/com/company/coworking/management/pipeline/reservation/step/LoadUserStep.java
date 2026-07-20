package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.entity.User;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUserStep implements ReservationStep {

    private final CurrentUserService currentUserService;

    @Override
    public void execute(CreateReservationContext context) {
        User user = currentUserService.getCurrentUser();

        context.setUser(user);
    }
}