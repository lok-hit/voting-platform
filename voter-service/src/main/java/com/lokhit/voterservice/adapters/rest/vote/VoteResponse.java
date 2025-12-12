package com.lokhit.voterservice.adapters.rest.vote;


import com.lokhit.voterservice.domain.model.Vote;

public record VoteResponse(String id, String voterId, String electionId, String optionId, String castAt) {
    public static VoteResponse from(Vote v) {
        return new VoteResponse(
                v.id().value().toString(),
                v.voterId().value().toString(),
                v.electionId().value(),
                v.optionId().value(),
                v.castAt().toString()
        );
    }
}

