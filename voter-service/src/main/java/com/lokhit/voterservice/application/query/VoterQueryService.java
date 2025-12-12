package com.lokhit.voterservice.application.query;

import java.util.List;

import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.model.Vote;
import com.lokhit.voterservice.domain.port.VoteRepository;
import com.lokhit.voterservice.domain.port.VoterRepository;

public final class VoterQueryService {
    private final VoterRepository voters;
    private final VoteRepository votes;

    public VoterQueryService(VoterRepository voters, VoteRepository votes) {
        this.voters = voters; this.votes = votes;
    }

    public Voter get(String voterIdRaw) {
        var id = new VoterId(java.util.UUID.fromString(voterIdRaw));
        return voters.findById(id).orElseThrow(() -> new IllegalArgumentException("Voter not found"));
    }

    public List<Vote> listVotes(String voterIdRaw) {
        var id = new VoterId(java.util.UUID.fromString(voterIdRaw));
        return votes.findByVoter(id);
    }
}

