package com.lokhit.voterservice.adapters.rest.vote;

import com.lokhit.voterservice.application.command.CastVoteCommandHandler;
import com.lokhit.voterservice.application.query.VoterQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/votes")
public class VoteController {
    private final CastVoteCommandHandler castHandler;
    private final VoterQueryService queryService;

    public VoteController(CastVoteCommandHandler c, VoterQueryService q) {
        this.castHandler = c; this.queryService = q;
    }

    @PostMapping
    public ResponseEntity<VoteResponse> cast(@RequestBody CastVoteRequest request) {
        var vote = castHandler.cast(request.voterId(), request.electionId(), request.optionId());
        return ResponseEntity.status(201).body(VoteResponse.from(vote));
    }

    @GetMapping("/voter/{voterId}")
    public ResponseEntity<VoteListResponse> listByVoter(@PathVariable String voterId) {
        var list = queryService.listVotes(voterId).stream()
                .map(VoteResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(new VoteListResponse(list));
    }
}


