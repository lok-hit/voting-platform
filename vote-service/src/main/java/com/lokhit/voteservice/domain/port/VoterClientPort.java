package com.lokhit.voteservice.domain.port;

public interface VoterClientPort {
    /**
     * Check that voter exists and is not blocked.
     */
    boolean isVoterAllowed(Long voterId);
}
