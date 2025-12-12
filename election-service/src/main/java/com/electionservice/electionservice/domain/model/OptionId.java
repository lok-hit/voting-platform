package com.electionservice.electionservice.domain.model;

import java.util.UUID;

public record OptionId(UUID value) {
    public static OptionId newId() { return new OptionId(UUID.randomUUID()); }
}


