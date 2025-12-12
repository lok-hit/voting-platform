package com.lokhit.voterservice.domain.event;


import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.values.Email;

public record VoterRegistered(VoterId voterId, Email email) implements DomainEvent { }

