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
        this.id = Objects.requireNonNull(id, "Election ID cannot be null");
        this.name = validateName(name);
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Creation timestamp cannot be null");
    }

    private static String validateName(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty or blank");
        }
        return name;
    }

    public static Election create(String name, TimeSource clock) {
        Objects.requireNonNull(clock, "TimeSource cannot be null");
        return new Election(ElectionId.newId(), name, ElectionStatus.OPEN, clock.now());
    }

    public void close() {
        this.status = ElectionStatus.CLOSED;
    }

    public void addOption(Option option) {
        Objects.requireNonNull(option, "Option cannot be null");
        
        if (status == ElectionStatus.CLOSED) {
            throw new IllegalStateException("Cannot add option to closed election");
        }
        
        if (options.stream().anyMatch(o -> o.equals(option))) {
            throw new IllegalStateException("Option already exists in this election");
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


