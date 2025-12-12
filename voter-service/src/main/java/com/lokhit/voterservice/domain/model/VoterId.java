package com.lokhit.voterservice.domain.model;


import java.util.UUID;

public record VoterId(UUID value) {
    public static VoterId newId() { return new VoterId(UUID.randomUUID()); }
    
    public static VoterId fromString(String uuid) {
        return new VoterId(UUID.fromString(uuid));
    }
}

