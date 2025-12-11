package com.lokhit.voteservice.domain.service;

import com.lokhit.voteservice.domain.model.Vote;
import com.lokhit.voteservice.domain.port.ElectionClientPort;
import com.lokhit.voteservice.domain.port.VoteRepositoryPort;
import com.lokhit.voteservice.domain.port.VoterClientPort;
import com.lokhit.voteservice.exception.ConflictException;
import com.lokhit.voteservice.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteDomainServiceTest {

    @Mock
    private VoteRepositoryPort voteRepository;

    @Mock
    private VoterClientPort voterClient;

    @Mock
    private ElectionClientPort electionClient;

    @Captor
    private ArgumentCaptor<Vote> voteCaptor;

    private VoteDomainService voteService;

    private final Long ELECTION_ID = 1L;
    private final Long VOTER_ID = 10L;
    private final Long OPTION_ID = 100L;

    @BeforeEach
    void setUp() {
        voteService = new VoteDomainService(voteRepository, voterClient, electionClient);
    }

    @Test
    void castVote_WhenAllConditionsMet_ShouldSaveVote() {
        // Given
        when(electionClient.existsElection(ELECTION_ID)).thenReturn(true);
        when(electionClient.isOptionInElection(ELECTION_ID, OPTION_ID)).thenReturn(true);
        when(voterClient.isVoterAllowed(VOTER_ID)).thenReturn(true);
        when(voteRepository.existsByElectionIdAndVoterId(ELECTION_ID, VOTER_ID)).thenReturn(false);

        // When
        voteService.castVote(ELECTION_ID, VOTER_ID, OPTION_ID);

        // Then
        verify(voteRepository).save(voteCaptor.capture());
        Vote savedVote = voteCaptor.getValue();
        assertAll(
            () -> assertEquals(ELECTION_ID, savedVote.getElectionId()),
            () -> assertEquals(OPTION_ID, savedVote.getOptionId()),
            () -> assertEquals(VOTER_ID, savedVote.getVoterId()),
            () -> assertNotNull(savedVote.getCreatedAt())
        );
    }

    @Test
    void castVote_WhenElectionDoesNotExist_ShouldThrowNotFoundException() {
        // Given
        when(electionClient.existsElection(ELECTION_ID)).thenReturn(false);

        // When & Then
        assertThrows(NotFoundException.class, 
            () -> voteService.castVote(ELECTION_ID, VOTER_ID, OPTION_ID));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void castVote_WhenOptionNotInElection_ShouldThrowNotFoundException() {
        // Given
        when(electionClient.existsElection(ELECTION_ID)).thenReturn(true);
        when(electionClient.isOptionInElection(ELECTION_ID, OPTION_ID)).thenReturn(false);

        // When & Then
        assertThrows(NotFoundException.class, 
            () -> voteService.castVote(ELECTION_ID, VOTER_ID, OPTION_ID));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void castVote_WhenVoterNotAllowed_ShouldThrowConflictException() {
        // Given
        when(electionClient.existsElection(ELECTION_ID)).thenReturn(true);
        when(electionClient.isOptionInElection(ELECTION_ID, OPTION_ID)).thenReturn(true);
        when(voterClient.isVoterAllowed(VOTER_ID)).thenReturn(false);

        // When & Then
        assertThrows(ConflictException.class, 
            () -> voteService.castVote(ELECTION_ID, VOTER_ID, OPTION_ID));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void castVote_WhenVoterAlreadyVoted_ShouldThrowConflictException() {
        // Given
        when(electionClient.existsElection(ELECTION_ID)).thenReturn(true);
        when(electionClient.isOptionInElection(ELECTION_ID, OPTION_ID)).thenReturn(true);
        when(voterClient.isVoterAllowed(VOTER_ID)).thenReturn(true);
        when(voteRepository.existsByElectionIdAndVoterId(ELECTION_ID, VOTER_ID)).thenReturn(true);

        // When & Then
        assertThrows(ConflictException.class, 
            () -> voteService.castVote(ELECTION_ID, VOTER_ID, OPTION_ID));
        verify(voteRepository, never()).save(any());
    }

    @Test
    void countVotesForOption_ShouldReturnCountFromRepository() {
        // Given
        long expectedCount = 5L;
        when(voteRepository.countByOptionId(OPTION_ID)).thenReturn(expectedCount);

        // When
        long actualCount = voteService.countVotesForOption(OPTION_ID);

        // Then
        assertEquals(expectedCount, actualCount);
        verify(voteRepository).countByOptionId(OPTION_ID);
    }
}
