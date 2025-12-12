package com.electionservice.electionservice.domain.port;

import com.electionservice.electionservice.domain.model.Election;
import com.electionservice.electionservice.domain.model.ElectionId;

import java.util.Optional;
import java.util.List;

public interface ElectionRepository {
    Election save(Election election);
    Optional<Election> findById(ElectionId id);
    List<Election> findAll();
}

