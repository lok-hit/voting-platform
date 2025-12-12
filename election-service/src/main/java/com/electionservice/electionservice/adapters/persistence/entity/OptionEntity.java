package com.electionservice.electionservice.adapters.persistence.entity;


import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "options")
public class OptionEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false, insertable = false, updatable = false)
    private ElectionEntity election;

    @Column(name = "election_id", nullable = false)
    private UUID electionId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElectionEntity getElection() {
        return election;
    }

    public void setElection(ElectionEntity election) {
        this.election = election;
    }
    
    public UUID getElectionId() {
        return electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }
}
