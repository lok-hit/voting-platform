package com.lokhit.voteservice.domain.port;

/**
 * Port (outbound) - election service client abstraction.
 * Implementacja adaptera (np. REST) powinna implementować ten interfejs.
 */
public interface ElectionClientPort {
    /**
     * Returns true if the election with given id exists.
     */
    boolean existsElection(Long electionId);

    /**
     * Returns true if the option belongs to the election.
     */
    boolean isOptionInElection(Long electionId, Long optionId);
}
