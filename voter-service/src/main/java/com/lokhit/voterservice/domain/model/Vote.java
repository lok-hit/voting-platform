package com.lokhit.voterservice.domain.model;



import com.lokhit.voterservice.domain.values.ElectionId;
import com.lokhit.voterservice.domain.values.OptionId;
import com.lokhit.voterservice.domain.values.TimeSource;

import java.time.Instant;
import java.util.Objects;

public final class Vote {
    private final VoteId id;
    private final VoterId voterId;
    private final ElectionId electionId;
    private final OptionId optionId;
    private final Instant castAt;

    public Vote(VoteId id, VoterId voterId, ElectionId electionId, OptionId optionId, Instant castAt) {
        this.id = id;
        this.voterId = voterId;
        this.electionId = electionId;
        this.optionId = optionId;
        this.castAt = castAt;
    }

    public static Vote cast(VoterId voterId, ElectionId electionId, OptionId optionId, TimeSource clock) {
        return new Vote(VoteId.newId(), voterId, electionId, optionId, clock.now());
    }

    public VoteId id() { return id; }
    public VoterId voterId() { return voterId; }
    public ElectionId electionId() { return electionId; }
    public OptionId optionId() { return optionId; }
    public Instant castAt() { return castAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote)) return false;
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}


