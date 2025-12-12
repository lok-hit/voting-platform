package com.lokhit.voterservice.domain.event;

import com.lokhit.voterservice.domain.model.VoterId;

public record VoterBlocked(VoterId voterId) implements DomainEvent { }

