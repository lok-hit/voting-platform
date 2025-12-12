package com.electionservice.electionservice.application.command;

import com.electionservice.electionservice.domain.event.OptionAdded;
import com.electionservice.electionservice.domain.model.*;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import com.electionservice.electionservice.domain.port.OptionRepository;
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

class AddOptionCommandHandlerTest {

    @Mock
    private ElectionRepository electionRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    private AddOptionCommandHandler handler;
    private ElectionId electionId;
    private String electionIdStr;
    private String optionName;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new AddOptionCommandHandler(electionRepository, optionRepository, eventPublisher);
        electionId = new ElectionId(UUID.randomUUID());
        electionIdStr = electionId.value().toString();
        optionName = "Test Option";
    }

    @Test
    void shouldAddOptionToElection() {
        // given
        Election election = new Election(electionId, "Test Election",
                ElectionStatus.OPEN, Instant.now());

        when(electionRepository.findById(electionId)).thenReturn(Optional.of(election));
        when(optionRepository.save(any(Option.class), eq(electionId))).thenAnswer(invocation ->
                invocation.getArgument(0) // Return the option passed to save
        );

        // when
        Option result = handler.add(electionIdStr, optionName);

        // then
        assertNotNull(result);
        assertEquals(optionName, result.name());

        ArgumentCaptor<Option> optionCaptor = ArgumentCaptor.forClass(Option.class);
        verify(optionRepository).save(optionCaptor.capture(), eq(electionId));

        Option savedOption = optionCaptor.getValue();
        assertEquals(optionName, savedOption.name());

        // Verify event was published
        verify(eventPublisher).publish(any(OptionAdded.class));
    }

    @Test
    void shouldNotAddOptionToNonExistentElection() {
        // given
        when(electionRepository.findById(any(ElectionId.class))).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> handler.add(electionIdStr, optionName));
        verify(optionRepository, never()).save(any(), any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldNotAddOptionToClosedElection() {
        // given
        Election closedElection = new Election(electionId, "Test Election",
                ElectionStatus.CLOSED, Instant.now());

        when(electionRepository.findById(electionId)).thenReturn(Optional.of(closedElection));

        // when & then
        assertThrows(IllegalStateException.class,
                () -> handler.add(electionIdStr, optionName));
        verify(optionRepository, never()).save(any(), any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldNotAddEmptyOptionName() {
        // given
        Election election = new Election(electionId, "Test Election",
                ElectionStatus.OPEN, Instant.now());

        when(electionRepository.findById(electionId)).thenReturn(Optional.of(election));

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> handler.add(electionIdStr, ""));
        verify(optionRepository, never()).save(any(), any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void shouldNotAddNullOptionName() {
        // given
        Election election = new Election(electionId, "Test Election",
                ElectionStatus.OPEN, Instant.now());

        when(electionRepository.findById(electionId)).thenReturn(Optional.of(election));

        // when & then
        assertThrows(NullPointerException.class,
                () -> handler.add(electionIdStr, null));
        verify(optionRepository, never()).save(any(), any());
        verifyNoInteractions(eventPublisher);
    }
}