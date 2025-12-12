package com.electionservice.electionservice.domain.port;

import com.electionservice.electionservice.domain.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}

