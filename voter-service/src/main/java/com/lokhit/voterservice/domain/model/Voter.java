package com.lokhit.voterservice.domain.model;


import com.lokhit.voterservice.domain.values.Email;
import com.lokhit.voterservice.domain.values.TimeSource;

import java.time.Instant;
import java.util.Objects;

public final class Voter {
    private final VoterId id;
    private final Email email;
    private VoterStatus status;
    private final Instant registeredAt;

    public Voter(VoterId id, Email email, VoterStatus status, Instant registeredAt) {
        this.id = id;
        this.email = email;
        this.status = status;
        this.registeredAt = registeredAt;
    }

    public static Voter register(Email email, TimeSource clock) {
        return new Voter(VoterId.newId(), email, VoterStatus.ACTIVE, clock.now());
    }

    public void block() { this.status = VoterStatus.BLOCKED; }
    public void unblock() { this.status = VoterStatus.ACTIVE; }
    public boolean isActive() { return status == VoterStatus.ACTIVE; }

    public VoterId id() { return id; }
    public Email email() { return email; }
    public VoterStatus status() { return status; }
    public Instant registeredAt() { return registeredAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Voter)) return false;
        Voter voter = (Voter) o;
        return Objects.equals(id, voter.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}

