package com.lokhit.voterservice.adapters.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "votes", uniqueConstraints = @UniqueConstraint(columnNames = {"voter_id", "election_id"}))
public class VoteEntity {
    @Id
    private UUID id;

    @Column(name = "voter_id", nullable = false)
    private UUID voterId;

    @Column(name = "election_id", nullable = false)
    private String electionId;

    @Column(name = "option_id", nullable = false)
    private String optionId;

    @Column(name = "cast_at", nullable = false)
    private Instant castAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getVoterId() { return voterId; }
    public void setVoterId(UUID voterId) { this.voterId = voterId; }
    public String getElectionId() { return electionId; }
    public void setElectionId(String electionId) { this.electionId = electionId; }
    public String getOptionId() { return optionId; }
    public void setOptionId(String optionId) { this.optionId = optionId; }
    public Instant getCastAt() { return castAt; }
    public void setCastAt(Instant castAt) { this.castAt = castAt; }
}

