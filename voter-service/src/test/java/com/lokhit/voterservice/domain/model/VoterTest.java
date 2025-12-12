package com.lokhit.voterservice.domain.model;

import com.lokhit.voterservice.domain.values.Email;
import com.lokhit.voterservice.domain.values.TimeSource;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class VoterTest {
    private static final TimeSource FIXED_CLOCK = () -> Instant.parse("2023-01-01T00:00:00Z");

    @Test
    void register_createsActiveVoter() {
        var email = new Email("test@example.com");
        var voter = Voter.register(email, FIXED_CLOCK);
        
        assertNotNull(voter.id());
        assertEquals(email, voter.email());
        assertEquals(VoterStatus.ACTIVE, voter.status());
        assertEquals(FIXED_CLOCK.now(), voter.registeredAt());
        assertTrue(voter.isActive());
    }

    @Test
    void block_setsStatusToBlocked() {
        var voter = createTestVoter();
        voter.block();
        
        assertEquals(VoterStatus.BLOCKED, voter.status());
        assertFalse(voter.isActive());
    }

    @Test
    void unblock_setsStatusToActive() {
        var voter = createTestVoter();
        voter.block();
        voter.unblock();
        
        assertEquals(VoterStatus.ACTIVE, voter.status());
        assertTrue(voter.isActive());
    }

    private Voter createTestVoter() {
        return new Voter(
            VoterId.newId(),
            new Email("test@example.com"),
            VoterStatus.ACTIVE,
            FIXED_CLOCK.now()
        );
    }
}
