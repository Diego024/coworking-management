package com.company.coworking.management.dto.request;

import com.company.coworking.management.util.enums.SpaceType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EditSpaceRequest {
    @NotBlank
    private String name;

    @NotNull
    private SpaceType spaceType;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotBlank
    private String location;

    @NotNull
    private BigDecimal hourlyRate;
}
