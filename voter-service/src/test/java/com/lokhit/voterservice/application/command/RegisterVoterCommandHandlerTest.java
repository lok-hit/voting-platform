package com.lokhit.voterservice.application.command;

import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.port.DomainEventPublisher;
import com.lokhit.voterservice.domain.port.VoterRepository;
import com.lokhit.voterservice.domain.values.Email;
import com.lokhit.voterservice.domain.values.TimeSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterVoterCommandHandlerTest {

    @Mock
    private VoterRepository voterRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @Captor
    private ArgumentCaptor<Voter> voterCaptor;

    @InjectMocks
    private RegisterVoterCommandHandler handler;

    private final TimeSource fixedClock = Instant::now;

    @BeforeEach
    void setUp() {
        handler = new RegisterVoterCommandHandler(voterRepository, eventPublisher, fixedClock);
    }

    @Test
    void register_newEmail_createsAndSavesVoter() {
        String email = "new@example.com";
        when(voterRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(voterRepository.save(any(Voter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Voter result = handler.register(email);

        verify(voterRepository).save(voterCaptor.capture());
        Voter savedVoter = voterCaptor.getValue();

        assertNotNull(savedVoter);
        assertEquals(email, savedVoter.email().value());
        assertTrue(savedVoter.isActive());
        verify(eventPublisher).publish(any());
    }

    @Test
    void register_existingEmail_throwsException() {
        String email = "existing@example.com";
        Voter existing = Voter.register(new Email(email), fixedClock);
        when(voterRepository.findByEmail(any())).thenReturn(Optional.of(existing));

        assertThrows(IllegalStateException.class, () -> handler.register(email));
        verify(voterRepository, never()).save(any());
    }
}