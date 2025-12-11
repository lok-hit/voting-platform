package com.lokhit.voteservice.domain.model;

import java.time.OffsetDateTime;

/**
 * Represents a vote cast by a voter for a specific option in an election.
 * This domain model captures the essential information about a single vote,
 * including references to the election, selected option, and the voter,
 * along with the timestamp when the vote was cast.
 */
public class Vote {
    /** Unique identifier for the vote */
    private Long id;
    
    /** ID of the election this vote is associated with */
    private Long electionId;
    
    /** ID of the option that was selected in this vote */
    private Long optionId;
    
    /** ID of the voter who cast this vote */
    private Long voterId;
    
    /** Timestamp when the vote was cast */
    private OffsetDateTime createdAt;

    /**
     * Creates a new Vote instance with the current timestamp.
     *
     * @param electionId the ID of the election
     * @param optionId the ID of the selected option
     * @param voterId the ID of the voter
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public Vote(Long electionId, Long optionId, Long voterId) {
        this.electionId = electionId;
        this.optionId = optionId;
        this.voterId = voterId;
        this.createdAt = OffsetDateTime.now();
    }

    /**
     * Creates a new Vote instance with all fields, including the creation timestamp.
     * Primarily used for reconstituting existing votes from persistent storage.
     *
     * @param id the unique identifier of the vote
     * @param electionId the ID of the election
     * @param optionId the ID of the selected option
     * @param voterId the ID of the voter
     * @param createdAt the timestamp when the vote was created
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public Vote(Long id, Long electionId, Long optionId, Long voterId, OffsetDateTime createdAt) {
        this.id = id;
        this.electionId = electionId;
        this.optionId = optionId;
        this.voterId = voterId;
        this.createdAt = createdAt;
    }

    /**
     * @return the unique identifier of this vote
     */
    public Long getId() { return id; }
    
    /**
     * @return the ID of the election this vote is associated with
     */
    public Long getElectionId() { return electionId; }
    
    /**
     * @return the ID of the option that was selected in this vote
     */
    public Long getOptionId() { return optionId; }
    
    /**
     * @return the ID of the voter who cast this vote
     */
    public Long getVoterId() { return voterId; }
    
    /**
     * @return the timestamp when this vote was created
     */
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
