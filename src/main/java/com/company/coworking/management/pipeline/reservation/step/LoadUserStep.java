package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.entity.User;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUserStep implements ReservationStep {

    private final UserRepository userRepository;

    @Override
    public void execute(CreateReservationContext context) {
        User user = userRepository.findById(context.getRequest().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + context.getRequest().getUserId()));

        context.setUser(user);
    }
}