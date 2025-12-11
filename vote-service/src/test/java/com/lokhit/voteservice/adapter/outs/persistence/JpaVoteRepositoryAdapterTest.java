package com.lokhit.voteservice.adapter.outs.persistence;

import com.lokhit.voteservice.adapter.outs.VoteEntity;
import com.lokhit.voteservice.domain.model.Vote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaVoteRepositoryAdapterTest {

    @Mock
    private SpringDataVoteRepository repository;

    @InjectMocks
    private JpaVoteRepositoryAdapter adapter;

    @Captor
    private ArgumentCaptor<VoteEntity> voteEntityCaptor;

    private final Long VOTE_ID = 1L;
    private final Long ELECTION_ID = 100L;
    private final Long OPTION_ID = 200L;
    private final Long VOTER_ID = 300L;
    private final OffsetDateTime CREATED_AT = OffsetDateTime.now();

    @Test
    void existsByElectionIdAndVoterId_ShouldReturnTrueWhenExists() {
        // Given
        when(repository.existsByElectionIdAndVoterId(ELECTION_ID, VOTER_ID)).thenReturn(true);

        // When
        boolean exists = adapter.existsByElectionIdAndVoterId(ELECTION_ID, VOTER_ID);

        // Then
        assertTrue(exists);
        verify(repository).existsByElectionIdAndVoterId(ELECTION_ID, VOTER_ID);
    }

    @Test
    void save_ShouldMapAndSaveVote() {
        // Given
        Vote vote = new Vote(null, ELECTION_ID, OPTION_ID, VOTER_ID);
        VoteEntity savedEntity = new VoteEntity();
        savedEntity.setId(VOTE_ID);
        savedEntity.setElectionId(ELECTION_ID);
        savedEntity.setOptionId(OPTION_ID);
        savedEntity.setVoterId(VOTER_ID);
        savedEntity.setCreatedAt(CREATED_AT);

        when(repository.save(any(VoteEntity.class))).thenReturn(savedEntity);

        // When
        Vote result = adapter.save(vote);

        // Then
        verify(repository).save(voteEntityCaptor.capture());
        VoteEntity capturedEntity = voteEntityCaptor.getValue();
        
        assertAll(
            () -> assertEquals(ELECTION_ID, capturedEntity.getElectionId()),
            () -> assertEquals(OPTION_ID, capturedEntity.getOptionId()),
            () -> assertEquals(VOTER_ID, capturedEntity.getVoterId()),
            () -> assertNotNull(capturedEntity.getCreatedAt()),
            
            () -> assertEquals(VOTE_ID, result.getId()),
            () -> assertEquals(ELECTION_ID, result.getElectionId()),
            () -> assertEquals(OPTION_ID, result.getOptionId()),
            () -> assertEquals(VOTER_ID, result.getVoterId()),
            () -> assertEquals(CREATED_AT, result.getCreatedAt())
        );
    }

    @Test
    void countByOptionId_ShouldReturnCountFromRepository() {
        // Given
        long expectedCount = 5L;
        when(repository.countByOptionId(OPTION_ID)).thenReturn(expectedCount);

        // When
        long actualCount = adapter.countByOptionId(OPTION_ID);

        // Then
        assertEquals(expectedCount, actualCount);
        verify(repository).countByOptionId(OPTION_ID);
    }
}
