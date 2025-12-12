package com.electionservice.electionservice.adapters.messaging;

import com.electionservice.electionservice.domain.event.DomainEvent;
import com.electionservice.electionservice.domain.port.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SimpleEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher springPublisher;

    public SimpleEventPublisher(ApplicationEventPublisher springPublisher) {
        this.springPublisher = springPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        springPublisher.publishEvent(event);
    }
}

