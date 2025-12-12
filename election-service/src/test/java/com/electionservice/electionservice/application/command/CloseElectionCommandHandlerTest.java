package com.electionservice.electionservice.application.command;

import com.electionservice.electionservice.domain.model.*;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import com.electionservice.electionservice.domain.port.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CloseElectionCommandHandlerTest {

    @Mock
    private ElectionRepository electionRepository;
    
    @Mock
    private DomainEventPublisher eventPublisher;
    
    private CloseElectionCommandHandler handler;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new CloseElectionCommandHandler(electionRepository, eventPublisher);
    }
    
    @Test
    void shouldCloseExistingElection() {
        // given
        ElectionId electionId = new ElectionId(UUID.randomUUID());
        Election election = new Election(electionId, "Test Election", 
                                       ElectionStatus.OPEN, Instant.now());
        
        when(electionRepository.findById(electionId)).thenReturn(Optional.of(election));
        
        // when
        handler.close(electionId.toString());
        
        // then
        ArgumentCaptor<Election> electionCaptor = ArgumentCaptor.forClass(Election.class);
        verify(electionRepository).save(electionCaptor.capture());
        
        Election savedElection = electionCaptor.getValue();
        assertEquals(ElectionStatus.CLOSED, savedElection.status());
        
        // Verify event was published
        verify(eventPublisher).publish(any());
    }
    
    @Test
    void shouldThrowWhenElectionNotFound() {
        // given
        ElectionId nonExistentId = new ElectionId(UUID.randomUUID());
        when(electionRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // when & then
        assertThrows(IllegalArgumentException.class, () -> handler.close(nonExistentId.toString()));
        verify(electionRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }
    
    @Test
    void shouldNotCloseAlreadyClosedElection() {
        // given
        ElectionId electionId = new ElectionId(UUID.randomUUID());
        Election closedElection = new Election(electionId, "Test Election", 
                                             ElectionStatus.CLOSED, Instant.now());
        
        when(electionRepository.findById(electionId)).thenReturn(Optional.of(closedElection));
        
        // when & then
        assertThrows(IllegalStateException.class, () -> handler.close(electionId.toString()));
        verify(electionRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }
}
