package com.lokhit.voterservice.domain.event;


import com.lokhit.voterservice.domain.model.VoterId;

public record VoterUnblocked(VoterId voterId) implements DomainEvent { }

