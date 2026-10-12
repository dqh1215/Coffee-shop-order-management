package com.architecture_solution.coffeeshop_order_management.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @JsonProperty("status_code")
    private int statusCode;

    private int code;

    private String error;

    private String message;

    private String path;

    private Instant timestamp;
}
