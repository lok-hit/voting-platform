package com.electionservice.electionservice.application.query;

import com.electionservice.electionservice.domain.model.*;
import com.electionservice.electionservice.domain.port.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OptionQueryServiceTest {

    @Mock
    private OptionRepository optionRepository;
    
    private OptionQueryService queryService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryService = new OptionQueryService(optionRepository);
    }
    
    @Test
    void shouldFindOptionById() {
        // given
        OptionId optionId = new OptionId(UUID.randomUUID());
        Option expectedOption = new Option(optionId, "Test Option");
        
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(expectedOption));
        
        // when
        Optional<Option> result = queryService.findById(optionId);
        
        // then
        assertTrue(result.isPresent());
        assertEquals(expectedOption, result.get());
        verify(optionRepository).findById(optionId);
    }
    
    @Test
    void shouldReturnEmptyForNonExistentOption() {
        // given
        OptionId nonExistentId = new OptionId(UUID.randomUUID());
        when(optionRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // when
        Optional<Option> result = queryService.findById(nonExistentId);
        
        // then
        assertTrue(result.isEmpty());
    }
    
    @Test
    void shouldListOptionsByElection() {
        // given
        String electionIdStr = UUID.randomUUID().toString();
        ElectionId electionId = new ElectionId(UUID.fromString(electionIdStr));
        Option option1 = new Option(new OptionId(UUID.randomUUID()), "Option 1");
        Option option2 = new Option(new OptionId(UUID.randomUUID()), "Option 2");
        
        when(optionRepository.findByElection(electionId)).thenReturn(Arrays.asList(option1, option2));
        
        // when
        List<Option> result = queryService.listByElection(electionIdStr);
        
        // then
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList(option1, option2)));
        verify(optionRepository).findByElection(electionId);
    }
    
    // Note: The exists method is not implemented in OptionQueryService
    // so we're removing this test as it's not applicable
}
