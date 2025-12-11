package com.lokhit.voteservice.adapter.outs.persistence;

import com.lokhit.voteservice.adapter.outs.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataVoteRepository extends JpaRepository<VoteEntity, Long> {
    boolean existsByElectionIdAndVoterId(Long electionId, Long voterId);
    long countByOptionId(Long optionId);
}
