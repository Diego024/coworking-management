package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.entity.Reservation;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.repository.ReservationRepository;
import com.company.coworking.management.service.integration.payment.PaymentValidationResult;
import com.company.coworking.management.service.state.reservation.PendingReservationState;
import com.company.coworking.management.util.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateReservationStep implements ReservationStep {

    private final ReservationRepository reservationRepository;

    @Override
    public void execute(CreateReservationContext context) {
        Reservation reservation = new Reservation();
        reservation.setUser(context.getUser());
        reservation.setSpace(context.getSpace());
        reservation.setStartTime(context.getRequest().getStartTime());
        reservation.setEndTime(context.getRequest().getEndTime());
        reservation.setPaymentMethod(context.getRequest().getPaymentMethod());
        reservation.setTotalPrice(context.getTotalPrice());

        if (context.getPaymentResult() == PaymentValidationResult.PENDING_CIRCUIT_OPEN) {
            reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        } else {
            reservation.setStatus(ReservationStatus.CONFIRMED);
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        context.setReservation(savedReservation);
    }
}