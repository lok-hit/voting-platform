package com.lokhit.voteservice.domain.model;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

class VoteTest {

    @Test
    void createVote_ShouldSetAllFields() {
        // Given
        Long id = 1L;
        Long electionId = 100L;
        Long optionId = 200L;
        Long voterId = 300L;
        OffsetDateTime now = OffsetDateTime.now();

        // When
        Vote vote = new Vote(id, electionId, optionId, voterId, now);

        // Then
        assertAll(
            () -> assertEquals(id, vote.getId()),
            () -> assertEquals(electionId, vote.getElectionId()),
            () -> assertEquals(optionId, vote.getOptionId()),
            () -> assertEquals(voterId, vote.getVoterId()),
            () -> assertEquals(now, vote.getCreatedAt())
        );
    }

    @Test
    void createVote_WithConstructor_ShouldSetCreatedAt() {
        // Given
        Long electionId = 100L;
        Long optionId = 200L;
        Long voterId = 300L;

        // When
        Vote vote = new Vote(electionId, optionId, voterId);

        // Then
        assertAll(
            () -> assertNull(vote.getId()),
            () -> assertEquals(electionId, vote.getElectionId()),
            () -> assertEquals(optionId, vote.getOptionId()),
            () -> assertEquals(voterId, vote.getVoterId()),
            () -> assertNotNull(vote.getCreatedAt()),
            () -> assertFalse(vote.getCreatedAt().isAfter(OffsetDateTime.now()))
        );
    }
}
