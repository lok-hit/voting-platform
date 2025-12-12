package com.electionservice.electionservice.adapters.persistence.repository;

import com.electionservice.electionservice.adapters.persistence.mapper.Mappers;
import com.electionservice.electionservice.domain.model.Election;
import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaElectionRepository implements ElectionRepository {
    private final SpringDataElectionJpa jpa;

    public JpaElectionRepository(SpringDataElectionJpa jpa) { this.jpa = jpa; }

    @Override
    public Election save(Election election) {
        return Mappers.toDomain(jpa.save(Mappers.toEntity(election)));
    }

    @Override
    public Optional<Election> findById(ElectionId id) {
        return jpa.findById(id.value()).map(Mappers::toDomain);
    }

    @Override
    public List<Election> findAll() {
        return jpa.findAll().stream().map(Mappers::toDomain).toList();
    }
}

