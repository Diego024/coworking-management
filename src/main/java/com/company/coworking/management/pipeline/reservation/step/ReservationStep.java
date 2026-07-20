package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;

public interface ReservationStep {

    /**
     * Ejecuta una porción específica de la lógica de creación de la reserva.
     * Modifica o enriquece el contexto según sea necesario.
     */
    void execute(CreateReservationContext context);
}