package com.electionservice.electionservice.application.query;

import com.electionservice.electionservice.domain.model.Election;
import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.model.ElectionStatus;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElectionQueryServiceTest {

    @Mock
    private ElectionRepository electionRepository;
    
    private ElectionQueryService queryService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryService = new ElectionQueryService(electionRepository);
    }
    
    @Test
    void shouldGetElectionById() {
        // given
        String electionIdStr = UUID.randomUUID().toString();
        ElectionId electionId = new ElectionId(UUID.fromString(electionIdStr));
        Election expectedElection = new Election(electionId, "Test Election", 
                                               ElectionStatus.OPEN, Instant.now());
        
        when(electionRepository.findById(electionId)).thenReturn(Optional.of(expectedElection));
        
        // when
        Election result = queryService.get(electionIdStr);
        
        // then
        assertNotNull(result);
        assertEquals(expectedElection, result);
        verify(electionRepository).findById(electionId);
    }
    
    @Test
    void shouldThrowExceptionForNonExistentElection() {
        // given
        String nonExistentId = UUID.randomUUID().toString();
        when(electionRepository.findById(any(ElectionId.class))).thenReturn(Optional.empty());
        
        // when & then
        assertThrows(IllegalArgumentException.class, 
                    () -> queryService.get(nonExistentId));
        verify(electionRepository).findById(any(ElectionId.class));
    }
    
    @Test
    void shouldListAllElections() {
        // given
        Election election1 = new Election(new ElectionId(UUID.randomUUID()), "Election 1", 
                                        ElectionStatus.OPEN, Instant.now());
        Election election2 = new Election(new ElectionId(UUID.randomUUID()), "Election 2", 
                                        ElectionStatus.CLOSED, Instant.now());
        
        when(electionRepository.findAll()).thenReturn(Arrays.asList(election1, election2));
        
        // when
        List<Election> result = queryService.listAll();
        
        // then
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(election1, election2)));
        verify(electionRepository).findAll();
    }
}
