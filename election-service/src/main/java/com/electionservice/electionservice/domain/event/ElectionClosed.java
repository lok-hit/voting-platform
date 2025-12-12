package com.electionservice.electionservice.domain.event;

import com.electionservice.electionservice.domain.model.ElectionId;

public record ElectionClosed(ElectionId electionId) implements DomainEvent { }

