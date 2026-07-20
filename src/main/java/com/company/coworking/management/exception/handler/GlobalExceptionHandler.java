package com.company.coworking.management.exception.handler;

import com.company.coworking.management.dto.response.GeneralResponseWithErrors;
import com.company.coworking.management.dto.response.ResponseBuilder;
import com.company.coworking.management.exception.business.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseBuilder.buildBadRequestResponse(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleConstraintViolationException(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        return ResponseBuilder.buildBadRequestResponse(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info(e.getMessage());
        return ResponseBuilder.buildBadRequestResponse("Invalid request body.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.info(e.getMessage());
        return ResponseBuilder.buildBadRequestResponse("Invalid request parameter.");
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseBuilder.buildUnauthorizedResponse("Invalid email or password.");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GeneralResponseWithErrors> handleException(Exception e) {
        log.error(e);
        return ResponseBuilder.buildInternalServerErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseBuilder.buildNotFoundResponse(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ResponseBuilder.buildConflictResponse(e.getMessage());
    }

    @ExceptionHandler(value = SpaceNotFoundException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleSpaceNotFoundException(SpaceNotFoundException e) {
        return ResponseBuilder.buildNotFoundResponse(e.getMessage());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseBuilder.buildNotFoundResponse(e.getMessage());
    }

    @ExceptionHandler(value = InvalidDateRangeException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleInvalidDateRangeException(InvalidDateRangeException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(value = InvalidReservationDurationException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleInvalidReservationDurationException(InvalidReservationDurationException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(value = PaymentDeclinedException.class)
    public ResponseEntity<GeneralResponseWithErrors> handlePaymentDeclinedException(PaymentDeclinedException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(value = SpaceAlreadyReservedException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleSpaceAlreadyReservedException(SpaceAlreadyReservedException e) {
        return ResponseBuilder.buildConflictResponse(e.getMessage());
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleReservationNotFoundException(ReservationNotFoundException e) {
        return ResponseBuilder.buildNotFoundResponse(e.getMessage());
    }

    @ExceptionHandler(ReservationCannotBeCancelledException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleReservationAlreadyCancelledException(ReservationCannotBeCancelledException e) {
        return ResponseBuilder.buildConflictResponse(e.getMessage());
    }
}