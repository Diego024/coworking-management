package com.company.coworking.management.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GeneralResponseWithErrors extends GeneralResponse {
    private List<String> errors;
}
