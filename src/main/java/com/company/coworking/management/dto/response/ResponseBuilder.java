package com.company.coworking.management.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class ResponseBuilder {

    private ResponseBuilder() {
        // Prevent instantiation
    }

    private static ResponseEntity<GeneralResponse> buildResponse(String message, HttpStatus status, Object data) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        return ResponseEntity.status(status).body(
                GeneralResponse.builder()
                        .status(status.value())
                        .message(message)
                        .data(data)
                        .uri(uri)
                        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))
                        .build()
        );
    }

    private static ResponseEntity<GeneralResponseWithErrors> buildErrorResponse(HttpStatus status, List<String> errors) {
        String uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        GeneralResponseWithErrors responseWithErrors = new GeneralResponseWithErrors();
        responseWithErrors.setUri(uri);
        responseWithErrors.setStatus(status.value());
        responseWithErrors.setErrors(errors);
        responseWithErrors.setTimestamp(LocalDateTime.now(ZoneId.systemDefault()));

        return ResponseEntity.status(status).body(responseWithErrors);
    }

    private static ResponseEntity<GeneralResponseWithErrors> buildErrorResponse(HttpStatus status, String error) {
        return buildErrorResponse(status, List.of(error));
    }

    public static ResponseEntity<GeneralResponse> buildAcceptedResponse(String message) {
        return buildResponse(message, HttpStatus.ACCEPTED, null);
    }

    public static ResponseEntity<GeneralResponseWithErrors> buildNotFoundResponse(String message) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, message);
    }

    public static ResponseEntity<GeneralResponseWithErrors> buildUnprocessableEntityResponse(String message) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    public static ResponseEntity<GeneralResponseWithErrors> buildConflictResponse(String message) {
        return buildErrorResponse(HttpStatus.CONFLICT, message);
    }

    public static ResponseEntity<GeneralResponseWithErrors> buildInternalServerErrorResponse(String message) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ResponseEntity<GeneralResponseWithErrors> buildBadRequestResponse(List<String> errors) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errors);
    }

    public static ResponseEntity<GeneralResponseWithErrors> buildBadRequestResponse(String errors) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, errors);
    }
}