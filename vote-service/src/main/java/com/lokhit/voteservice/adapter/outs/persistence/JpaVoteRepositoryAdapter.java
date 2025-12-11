package com.lokhit.voteservice.adapter.outs.persistence;

import com.lokhit.voteservice.adapter.outs.VoteEntity;
import com.lokhit.voteservice.domain.model.Vote;
import com.lokhit.voteservice.domain.port.VoteRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class JpaVoteRepositoryAdapter implements VoteRepositoryPort {

    private final SpringDataVoteRepository repo;

    public JpaVoteRepositoryAdapter(SpringDataVoteRepository repo) {
        this.repo = repo;
    }

    @Override
    public boolean existsByElectionIdAndVoterId(Long electionId, Long voterId) {
        return repo.existsByElectionIdAndVoterId(electionId, voterId);
    }

    @Override
    public Vote save(Vote vote) {
        VoteEntity e = new VoteEntity();
        e.setElectionId(vote.getElectionId());
        e.setOptionId(vote.getOptionId());
        e.setVoterId(vote.getVoterId());
        VoteEntity saved = repo.save(e);
        return new Vote(saved.getId(), saved.getElectionId(), saved.getOptionId(), saved.getVoterId(), saved.getCreatedAt());
    }

    @Override
    public long countByOptionId(Long optionId) {
        return repo.countByOptionId(optionId);
    }
}
