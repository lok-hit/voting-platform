package com.lokhit.voterservice.domain.port;

import com.lokhit.voterservice.domain.model.Vote;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.values.ElectionId;

import java.util.List;

public interface VoteRepository {
    boolean existsByVoterAndElection(VoterId voterId, ElectionId electionId);
    Vote save(Vote vote);
    List<Vote> findByVoter(VoterId voterId);
}

