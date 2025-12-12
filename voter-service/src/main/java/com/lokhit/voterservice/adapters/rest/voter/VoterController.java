package com.lokhit.voterservice.adapters.rest.voter;

import com.lokhit.voterservice.adapters.rest.common.ApiError;
import com.lokhit.voterservice.application.command.BlockVoterCommandHandler;
import com.lokhit.voterservice.application.command.RegisterVoterCommandHandler;
import com.lokhit.voterservice.application.command.UnblockVoterCommandHandler;
import com.lokhit.voterservice.application.query.VoterQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/voters")
public class VoterController {
    private final RegisterVoterCommandHandler registerHandler;
    private final BlockVoterCommandHandler blockHandler;
    private final UnblockVoterCommandHandler unblockHandler;
    private final VoterQueryService queryService;

    public VoterController(RegisterVoterCommandHandler r, BlockVoterCommandHandler b,
                           UnblockVoterCommandHandler u, VoterQueryService q) {
        this.registerHandler = r; this.blockHandler = b; this.unblockHandler = u; this.queryService = q;
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterVoterRequest request) {
        try {
            var voter = registerHandler.register(request.email());
            return ResponseEntity.status(201).body(VoterResponse.from(voter));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiError("INVALID_EMAIL", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<VoterResponse> block(@PathVariable("id") String id) {
        var voter = blockHandler.block(id);
        return ResponseEntity.ok(VoterResponse.from(voter));
    }

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<VoterResponse> unblock(@PathVariable("id") String id) {
        var voter = unblockHandler.unblock(id);
        return ResponseEntity.ok(VoterResponse.from(voter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoterResponse> get(@PathVariable("id") String id) {
        var voter = queryService.get(id);
        if (voter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(VoterResponse.from(voter));
    }
}
