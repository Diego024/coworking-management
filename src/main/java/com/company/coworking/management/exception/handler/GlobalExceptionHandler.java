package com.company.coworking.management.exception.handler;

import com.company.coworking.management.dto.response.GeneralResponseWithErrors;
import com.company.coworking.management.dto.response.ResponseBuilder;
import com.company.coworking.management.exception.business.EmailAlreadyExistsException;
import com.company.coworking.management.exception.business.SpaceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseBuilder.buildUnauthorizedResponse("Invalid email or password.");
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GeneralResponseWithErrors> handleException(Exception e) {
        log.error(e);
        return ResponseBuilder.buildInternalServerErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ResponseBuilder.buildConflictResponse(e.getMessage());
    }

    @ExceptionHandler(value = SpaceNotFoundException.class)
    public ResponseEntity<GeneralResponseWithErrors> handleSpaceNotFoundException(SpaceNotFoundException e) {
        return ResponseBuilder.buildNotFoundResponse(e.getMessage());
    }
}