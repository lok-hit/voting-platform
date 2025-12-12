package com.lokhit.voterservice.application.command;

import com.lokhit.voterservice.domain.event.VoterRegistered;
import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.port.DomainEventPublisher;
import com.lokhit.voterservice.domain.port.VoterRepository;
import com.lokhit.voterservice.domain.values.Email;
import com.lokhit.voterservice.domain.values.TimeSource;
import org.springframework.transaction.annotation.Transactional;

public final class RegisterVoterCommandHandler {
    private final VoterRepository voters;
    private final DomainEventPublisher events;
    private final TimeSource clock;

    public RegisterVoterCommandHandler(VoterRepository voters, DomainEventPublisher events, TimeSource clock) {
        this.voters = voters; this.events = events; this.clock = clock;
    }

    @Transactional
    public Voter register(String emailRaw) {
        var email = new Email(emailRaw);
        voters.findByEmail(email).ifPresent(v -> { throw new IllegalStateException("Email already registered"); });
        var voter = Voter.register(email, clock);
        var saved = voters.save(voter);
        events.publish(new VoterRegistered(saved.id(), saved.email()));
        return saved;
    }
}

