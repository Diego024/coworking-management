package com.company.coworking.management.service.validation.reservation;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;

public interface ReservationValidation {
    /**
     * Valida una regla de negocio específica.
     * Debe lanzar una excepción de negocio (business exception) si la validación falla.
     */
    void validate(CreateReservationContext context);
}