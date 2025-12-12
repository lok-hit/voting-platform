package com.electionservice.electionservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Election {
    private final ElectionId id;
    private final String name;
    private ElectionStatus status;
    private final Instant createdAt;
    private final List<Option> options = new ArrayList<>();

    public Election(ElectionId id, String name, ElectionStatus status, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Election create(String name, TimeSource clock) {
        return new Election(ElectionId.newId(), name, ElectionStatus.OPEN, clock.now());
    }

    public void close() {
        this.status = ElectionStatus.CLOSED;
    }

    public void addOption(Option option) {
        if (status == ElectionStatus.CLOSED) {
            throw new IllegalStateException("Cannot add option to closed election");
        }
        options.add(option);
    }

    public ElectionId id() { return id; }
    public String name() { return name; }
    public ElectionStatus status() { return status; }
    public Instant createdAt() { return createdAt; }
    public List<Option> options() { return List.copyOf(options); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Election)) return false;
        Election election = (Election) o;
        return Objects.equals(id, election.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}


