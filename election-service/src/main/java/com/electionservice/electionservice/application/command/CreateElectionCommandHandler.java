package com.electionservice.electionservice.application.command;

import com.electionservice.electionservice.domain.event.ElectionCreated;
import com.electionservice.electionservice.domain.model.Election;
import com.electionservice.electionservice.domain.model.TimeSource;
import com.electionservice.electionservice.domain.port.DomainEventPublisher;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import org.springframework.transaction.annotation.Transactional;

public final class CreateElectionCommandHandler {
    private final ElectionRepository elections;
    private final DomainEventPublisher events;
    private final TimeSource clock;

    public CreateElectionCommandHandler(ElectionRepository elections,
                                        DomainEventPublisher events,
                                        TimeSource clock) {
        this.elections = elections;
        this.events = events;
        this.clock = clock;
    }

    @Transactional
    public Election create(String name) {
        var election = Election.create(name, clock);
        var saved = elections.save(election);
        events.publish(new ElectionCreated(saved.id(), saved.name()));
        return saved;
    }
}

