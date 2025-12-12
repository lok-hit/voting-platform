package com.lokhit.voterservice.domain.event;

public sealed interface DomainEvent
        permits VoterRegistered, VoterBlocked, VoterUnblocked, VoteCast {
}

