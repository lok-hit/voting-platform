package com.lokhit.voterservice.adapters.messaging;

import com.lokhit.voterservice.domain.event.DomainEvent;
import com.lokhit.voterservice.domain.port.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

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

