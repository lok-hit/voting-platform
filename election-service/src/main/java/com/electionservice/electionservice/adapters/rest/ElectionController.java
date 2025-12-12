package com.electionservice.electionservice.adapters.rest;

import com.electionservice.electionservice.application.command.AddOptionCommandHandler;
import com.electionservice.electionservice.application.command.CloseElectionCommandHandler;
import com.electionservice.electionservice.application.command.CreateElectionCommandHandler;
import com.electionservice.electionservice.application.query.ElectionQueryService;
import com.electionservice.electionservice.application.query.OptionQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/elections")
public class ElectionController {
    private final CreateElectionCommandHandler createHandler;
    private final CloseElectionCommandHandler closeHandler;
    private final AddOptionCommandHandler addOptionHandler;
    private final ElectionQueryService electionQuery;
    private final OptionQueryService optionQuery;

    public ElectionController(CreateElectionCommandHandler c,
                              CloseElectionCommandHandler cl,
                              AddOptionCommandHandler a,
                              ElectionQueryService eq,
                              OptionQueryService oq) {
        this.createHandler = c; this.closeHandler = cl; this.addOptionHandler = a;
        this.electionQuery = eq; this.optionQuery = oq;
    }

    @PostMapping
    public ResponseEntity<ElectionResponse> create(@RequestBody CreateElectionRequest request) {
        var election = createHandler.create(request.name());
        return ResponseEntity.status(201).body(ElectionResponse.from(election));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<ElectionResponse> close(@PathVariable String id) {
        var election = closeHandler.close(id);
        return ResponseEntity.ok(ElectionResponse.from(election));
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<OptionResponse> addOption(@PathVariable String id, @RequestBody OptionRequest request) {
        var option = addOptionHandler.add(id, request.name());
        return ResponseEntity.status(201).body(OptionResponse.from(option));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectionResponse> get(@PathVariable String id) {
        var election = electionQuery.get(id);
        return ResponseEntity.ok(ElectionResponse.from(election));
    }

    @GetMapping
    public ResponseEntity<java.util.List<ElectionResponse>> listAll() {
        var list = electionQuery.listAll().stream().map(ElectionResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<java.util.List<OptionResponse>> listOptions(@PathVariable String id) {
        var list = optionQuery.listByElection(id).stream().map(OptionResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}

