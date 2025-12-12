package com.lokhit.voterservice.application.command;


import com.lokhit.voterservice.domain.event.VoterUnblocked;
import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.port.DomainEventPublisher;
import com.lokhit.voterservice.domain.port.VoterRepository;
import org.springframework.transaction.annotation.Transactional;

public final class UnblockVoterCommandHandler {
    private final VoterRepository voters;
    private final DomainEventPublisher events;

    public UnblockVoterCommandHandler(VoterRepository voters, DomainEventPublisher events) {
        this.voters = voters; this.events = events;
    }

    @Transactional
    public Voter unblock(String voterIdRaw) {
        var id = new VoterId(java.util.UUID.fromString(voterIdRaw));
        var voter = voters.findById(id).orElseThrow(() -> new IllegalArgumentException("Voter not found"));
        voter.unblock();
        var saved = voters.save(voter);
        events.publish(new VoterUnblocked(saved.id()));
        return saved;
    }
}


