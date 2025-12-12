package com.lokhit.voterservice.application.command;


import com.lokhit.voterservice.domain.event.VoterBlocked;
import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.port.DomainEventPublisher;
import com.lokhit.voterservice.domain.port.VoterRepository;
import org.springframework.transaction.annotation.Transactional;

public final class BlockVoterCommandHandler {
    private final VoterRepository voters;
    private final DomainEventPublisher events;

    public BlockVoterCommandHandler(VoterRepository voters, DomainEventPublisher events) {
        this.voters = voters; this.events = events;
    }

    @Transactional
    public Voter block(String voterIdRaw) {
        var id = new VoterId(java.util.UUID.fromString(voterIdRaw));
        var voter = voters.findById(id).orElseThrow(() -> new IllegalArgumentException("Voter not found"));
        voter.block();
        var saved = voters.save(voter);
        events.publish(new VoterBlocked(saved.id()));
        return saved;
    }
}

