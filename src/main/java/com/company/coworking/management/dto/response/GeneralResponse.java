package com.company.coworking.management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar de la API.")
public class GeneralResponse {

    @Schema(
            description = "URI del recurso solicitado.",
            example = "http://localhost:8080/api/v1/balance/process-transaction"
    )
    private String uri;

    @Schema(
            description = "Mensaje descriptivo de la operación.",
            example = "Transacción recibida correctamente."
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @Schema(
            description = "Código HTTP de la respuesta.",
            example = "202"
    )
    private int status;

    @Schema(description = "Fecha y hora de generación de la respuesta.")
    private LocalDateTime timestamp;

    @Schema(description = "Información adicional de la respuesta.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
}