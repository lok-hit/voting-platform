package com.electionservice.electionservice.domain.event;

public sealed interface DomainEvent permits ElectionCreated, ElectionClosed, OptionAdded { }

