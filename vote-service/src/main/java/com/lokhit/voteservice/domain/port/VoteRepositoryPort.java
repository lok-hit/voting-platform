package com.lokhit.voteservice.domain.port;


import com.lokhit.voteservice.domain.model.Vote;

public interface VoteRepositoryPort {
    boolean existsByElectionIdAndVoterId(Long electionId, Long voterId);
    Vote save(Vote vote);
    long countByOptionId(Long optionId);
}
