package com.lokhit.voterservice.domain.model;

import com.lokhit.voterservice.domain.values.ElectionId;
import com.lokhit.voterservice.domain.values.OptionId;
import com.lokhit.voterservice.domain.values.TimeSource;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class VoteTest {
    private static final TimeSource FIXED_CLOCK = () -> Instant.parse("2023-01-01T00:00:00Z");

    @Test
    void cast_createsVoteWithCurrentTimestamp() {
        var voterId = VoterId.newId();
        var electionId = new ElectionId("election-1");
        var optionId = new OptionId("option-1");
        
        var vote = Vote.cast(voterId, electionId, optionId, FIXED_CLOCK);
        
        assertNotNull(vote.id());
        assertEquals(voterId, vote.voterId());
        assertEquals(electionId, vote.electionId());
        assertEquals(optionId, vote.optionId());
        assertEquals(FIXED_CLOCK.now(), vote.castAt());
    }
}
