package com.lokhit.voterservice.application.command;

import com.lokhit.voterservice.domain.event.VoteCast;
import com.lokhit.voterservice.domain.model.Vote;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.port.DomainEventPublisher;
import com.lokhit.voterservice.domain.port.ElectionCatalog;
import com.lokhit.voterservice.domain.port.VoteRepository;
import com.lokhit.voterservice.domain.port.VoterRepository;
import com.lokhit.voterservice.domain.values.ElectionId;
import com.lokhit.voterservice.domain.values.OptionId;
import com.lokhit.voterservice.domain.values.TimeSource;
import org.springframework.transaction.annotation.Transactional;

public final class CastVoteCommandHandler {
    private final VoterRepository voters;
    private final VoteRepository votes;
    private final ElectionCatalog elections;
    private final DomainEventPublisher events;
    private final TimeSource clock;

    public CastVoteCommandHandler(VoterRepository voters, VoteRepository votes,
                                  ElectionCatalog elections, DomainEventPublisher events, TimeSource clock) {
        this.voters = voters; this.votes = votes; this.elections = elections; this.events = events; this.clock = clock;
    }

    @Transactional
    public Vote cast(String voterIdRaw, String electionIdRaw, String optionIdRaw) {
        var voterId = new VoterId(java.util.UUID.fromString(voterIdRaw));
        var electionId = new ElectionId(electionIdRaw);
        var optionId = new OptionId(optionIdRaw);

        var voter = voters.findById(voterId).orElseThrow(() -> new IllegalArgumentException("Voter not found"));
        if (!voter.isActive()) throw new IllegalStateException("Voter is blocked");

        if (!elections.electionExists(electionId)) throw new IllegalArgumentException("Election not found");
        if (!elections.optionExists(electionId, optionId)) throw new IllegalArgumentException("Option not found");

        if (votes.existsByVoterAndElection(voterId, electionId)) throw new IllegalStateException("Already voted in this election");

        var vote = Vote.cast(voterId, electionId, optionId, clock);
        var saved = votes.save(vote);
        events.publish(new VoteCast(saved.id(), voterId, electionId, optionId));
        return saved;
    }
}

