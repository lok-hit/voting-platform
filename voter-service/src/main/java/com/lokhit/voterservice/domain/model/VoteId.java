package com.lokhit.voterservice.domain.model;


import java.util.UUID;

public record VoteId(UUID value) {
    public static VoteId newId() { return new VoteId(UUID.randomUUID()); }
}
