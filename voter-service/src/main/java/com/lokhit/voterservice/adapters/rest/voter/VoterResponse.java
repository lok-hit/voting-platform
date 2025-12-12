package com.lokhit.voterservice.adapters.rest.voter;


import com.lokhit.voterservice.domain.model.Voter;

public record VoterResponse(String id, String email, String status, String registeredAt) {
    public static VoterResponse from(Voter voter) {
        return new VoterResponse(
                voter.id().value().toString(),
                voter.email().value(),
                voter.status().name(),
                voter.registeredAt().toString()
        );
    }
}

