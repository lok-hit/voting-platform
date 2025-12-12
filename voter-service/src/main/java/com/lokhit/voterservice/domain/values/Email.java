package com.lokhit.voterservice.domain.values;

import java.util.Objects;

public record Email(String value) {
    public Email {
        Objects.requireNonNull(value);
        String normalized = value.trim().toLowerCase();
        if (!EmailValidator.isValid(normalized)) {
            throw new IllegalArgumentException("Invalid email");
        }
        value = normalized;
    }
}

