package com.electionservice.electionservice.application.command;

import com.electionservice.electionservice.adapters.messaging.SimpleEventPublisher;
import com.electionservice.electionservice.domain.model.*;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import com.electionservice.electionservice.domain.port.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateElectionCommandHandlerTest {

    @Mock
    private ElectionRepository electionRepository;
    
    @Mock
    private DomainEventPublisher eventPublisher;
    
    private CreateElectionCommandHandler handler;
    private TimeSource timeSource;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        timeSource = () -> Instant.now().truncatedTo(ChronoUnit.MILLIS);
        handler = new CreateElectionCommandHandler(electionRepository, eventPublisher, timeSource);
    }
    
    @Test
    void shouldCreateNewElection() {
        // given
        String electionName = "Test Election";
        
        // when
        Election election = handler.create(electionName);
        ElectionId electionId = election.id();
        
        // then
        assertNotNull(electionId);
        
        ArgumentCaptor<Election> electionCaptor = ArgumentCaptor.forClass(Election.class);
        verify(electionRepository).save(electionCaptor.capture());
        
        Election savedElection = electionCaptor.getValue();
        assertNotNull(savedElection);
        assertEquals(electionName, savedElection.name());
        assertEquals(ElectionStatus.OPEN, savedElection.status());
        assertNotNull(savedElection.createdAt());
        assertTrue(savedElection.options().isEmpty());
        
        // Verify event was published
        verify(eventPublisher).publish(any());
    }
    
    @Test
    void shouldNotCreateElectionWithEmptyName() {
        // given
        String emptyName = "";
        
        // when & then
        assertThrows(IllegalArgumentException.class, () -> handler.create(emptyName));
        verifyNoInteractions(electionRepository);
        verifyNoInteractions(eventPublisher);
    }
    
    @Test
    void shouldNotCreateElectionWithNullName() {
        // when & then
        assertThrows(NullPointerException.class, () -> handler.create(null));
        verifyNoInteractions(electionRepository);
        verifyNoInteractions(eventPublisher);
    }
}
