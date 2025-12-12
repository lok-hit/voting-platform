package com.lokhit.voterservice.adapters.rest.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        String code,
        String message,
        LocalDateTime timestamp
) {
    public ApiError(String code, String message) {
        this(code, message, LocalDateTime.now());
    }
}
