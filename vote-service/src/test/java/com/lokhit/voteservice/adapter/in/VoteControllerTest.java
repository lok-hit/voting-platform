package com.lokhit.voteservice.adapter.in;

import com.lokhit.voteservice.application.dto.VoteRequest;
import com.lokhit.voteservice.domain.service.VoteDomainService;
import com.lokhit.voteservice.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteControllerTest {

    @Mock
    private VoteDomainService voteService;

    @InjectMocks
    private VoteController voteController;

    private static final Long ELECTION_ID = 1L;
    private static final Long VOTER_ID = 10L;
    private static final Long OPTION_ID = 100L;

    private VoteRequest voteRequest;

    @BeforeEach
    void setUp() {
        voteRequest = new VoteRequest();
        voteRequest.setVoterId(VOTER_ID);
        voteRequest.setOptionId(OPTION_ID);
    }

    @Test
    void vote_WhenRequestIsValid_ShouldReturnOk() {
        // Given - setup with valid request

        // When
        ResponseEntity<ApiResponse> response = voteController.vote(ELECTION_ID, voteRequest);

        // Then
        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            () -> assertNotNull(response.getBody()),
            () -> assertEquals("Vote recorded", response.getBody().getMessage())
        );
        
        verify(voteService).castVote(ELECTION_ID, VOTER_ID, OPTION_ID);
    }

    @Test
    void vote_WhenServiceThrowsException_ShouldPropagate() {
        // Given
        doThrow(new RuntimeException("Test exception"))
            .when(voteService).castVote(anyLong(), anyLong(), anyLong());

        // When & Then
        assertThrows(RuntimeException.class, 
            () -> voteController.vote(ELECTION_ID, voteRequest));
    }
}
