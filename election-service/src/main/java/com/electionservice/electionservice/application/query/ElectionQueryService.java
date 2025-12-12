package com.electionservice.electionservice.application.query;

import com.electionservice.electionservice.domain.model.Election;
import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.port.ElectionRepository;

import java.util.List;
import java.util.UUID;

public final class ElectionQueryService {
    private final ElectionRepository elections;

    public ElectionQueryService(ElectionRepository elections) {
        this.elections = elections;
    }

    public Election get(String electionIdRaw) {
        var id = new ElectionId(UUID.fromString(electionIdRaw));
        return elections.findById(id).orElseThrow(() -> new IllegalArgumentException("Election not found"));
    }

    public List<Election> listAll() {
        return elections.findAll();
    }
}

