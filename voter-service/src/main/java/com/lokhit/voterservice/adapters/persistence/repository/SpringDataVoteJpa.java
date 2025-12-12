package com.lokhit.voterservice.adapters.persistence.repository;

import com.lokhit.voterservice.adapters.persistence.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataVoteJpa extends JpaRepository<VoteEntity, UUID> {
    boolean existsByVoterIdAndElectionId(UUID voterId, String electionId);
    List<VoteEntity> findByVoterId(UUID voterId);
}

