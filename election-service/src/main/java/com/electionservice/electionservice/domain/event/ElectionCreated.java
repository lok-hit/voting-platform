package com.electionservice.electionservice.domain.event;

import com.electionservice.electionservice.domain.model.ElectionId;

public record ElectionCreated(ElectionId electionId, String name) implements DomainEvent { }

