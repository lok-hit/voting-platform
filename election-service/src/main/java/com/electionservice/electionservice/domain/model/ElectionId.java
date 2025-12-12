package com.electionservice.electionservice.domain.model;


import java.util.UUID;

public record ElectionId(UUID value) {
    public static ElectionId newId() { return new ElectionId(UUID.randomUUID()); }
}

