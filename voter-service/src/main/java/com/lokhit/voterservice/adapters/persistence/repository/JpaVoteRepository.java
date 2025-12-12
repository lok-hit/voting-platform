package com.lokhit.voterservice.adapters.persistence.repository;

import com.lokhit.voterservice.adapters.persistence.mapper.Mappers;
import com.lokhit.voterservice.domain.model.Vote;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.port.VoteRepository;
import com.lokhit.voterservice.domain.values.ElectionId;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class JpaVoteRepository implements VoteRepository {
    private final SpringDataVoteJpa jpa;

    public JpaVoteRepository(SpringDataVoteJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public boolean existsByVoterAndElection(VoterId voterId, ElectionId electionId) {
        return jpa.existsByVoterIdAndElectionId(voterId.value(), electionId.value());
    }

    @Override
    public java.util.List<Vote> findByVoter(VoterId voterId) {
        return jpa.findByVoterId(voterId.value()).stream()
                .map(Mappers::toDomain)
                .collect(Collectors.toList());
    }


    @Override
    public Vote save(Vote vote) {
        return Mappers.toDomain(jpa.save(Mappers.toEntity(vote)));
    }
}

