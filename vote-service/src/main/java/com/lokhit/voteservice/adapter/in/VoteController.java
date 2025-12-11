package com.lokhit.voteservice.adapter.in;


import com.lokhit.voteservice.application.dto.VoteRequest;
import com.lokhit.voteservice.domain.service.VoteDomainService;
import com.lokhit.voteservice.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/elections/{electionId}/vote")
public class VoteController {

    private final VoteDomainService voteService;

    public VoteController(VoteDomainService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> vote(@PathVariable Long electionId,
                                            @Valid @RequestBody VoteRequest req) {
        voteService.castVote(electionId, req.getVoterId(), req.getOptionId());
        return ResponseEntity.ok(new ApiResponse("Vote recorded"));
    }
}
