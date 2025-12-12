package com.electionservice.electionservice.application.command;
import com.electionservice.electionservice.domain.event.ElectionClosed;
import com.electionservice.electionservice.domain.model.Election;
import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.port.DomainEventPublisher;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public final class CloseElectionCommandHandler {
    private final ElectionRepository elections;
    private final DomainEventPublisher events;

    public CloseElectionCommandHandler(ElectionRepository elections,
                                       DomainEventPublisher events) {
        this.elections = elections;
        this.events = events;
    }

    @Transactional
    public Election close(String electionIdRaw) {
        var id = new ElectionId(UUID.fromString(electionIdRaw));
        var election = elections.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election not found"));
        election.close();
        var saved = elections.save(election);
        events.publish(new ElectionClosed(saved.id()));
        return saved;
    }
}

