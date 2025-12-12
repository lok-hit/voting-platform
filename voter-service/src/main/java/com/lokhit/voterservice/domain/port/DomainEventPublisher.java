package com.lokhit.voterservice.domain.port;


import com.lokhit.voterservice.domain.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
