package com.company.coworking.management.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Respuesta utilizada cuando ocurre uno o varios errores.")
public class GeneralResponseWithErrors extends GeneralResponse {
    @ArraySchema(arraySchema = @Schema(description = "Lista de errores encontrados"))
    private List<String> errors;
}
