package com.lokhit.voteservice.domain.service;
import com.lokhit.voteservice.domain.model.Vote;
import com.lokhit.voteservice.domain.port.ElectionClientPort;
import com.lokhit.voteservice.domain.port.VoteRepositoryPort;
import com.lokhit.voteservice.domain.port.VoterClientPort;
import com.lokhit.voteservice.exception.ConflictException;
import com.lokhit.voteservice.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VoteDomainService {

    private final VoteRepositoryPort voteRepo;
    private final VoterClientPort voterClient;
    private final ElectionClientPort electionClient;

    public VoteDomainService(VoteRepositoryPort voteRepo,
                             VoterClientPort voterClient,
                             ElectionClientPort electionClient) {
        this.voteRepo = voteRepo;
        this.voterClient = voterClient;
        this.electionClient = electionClient;
    }

    @Transactional
    public void castVote(Long electionId, Long voterId, Long optionId) {
        // 1. check election exists
        if (!electionClient.existsElection(electionId)) {
            throw new NotFoundException("Election not found: " + electionId);
        }

        // 2. check option belongs to election
        if (!electionClient.isOptionInElection(electionId, optionId)) {
            throw new NotFoundException("Option not found in election");
        }

        // 3. check voter allowed (exists & not blocked)
        if (!voterClient.isVoterAllowed(voterId)) {
            throw new ConflictException("Voter not allowed to vote (not found or blocked)");
        }

        // 4. check not already voted
        if (voteRepo.existsByElectionIdAndVoterId(electionId, voterId)) {
            throw new ConflictException("Voter has already voted in this election");
        }

        // 5. persist vote
        Vote v = new Vote(electionId, optionId, voterId);
        voteRepo.save(v);
    }

    public long countVotesForOption(Long optionId) {
        return voteRepo.countByOptionId(optionId);
    }
}