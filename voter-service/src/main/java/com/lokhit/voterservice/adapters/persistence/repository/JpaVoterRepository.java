package com.lokhit.voterservice.adapters.persistence.repository;


import com.lokhit.voterservice.adapters.persistence.mapper.Mappers;
import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.port.VoterRepository;
import com.lokhit.voterservice.domain.values.Email;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaVoterRepository implements VoterRepository {
    private final SpringDataVoterJpa jpa;
    public JpaVoterRepository(SpringDataVoterJpa jpa) { this.jpa = jpa; }

    @Override
    public Optional<Voter> findById(VoterId id) {
        return jpa.findById(id.value()).map(Mappers::toDomain);
    }

    @Override
    public Optional<Voter> findByEmail(Email email) {
        return jpa.findByEmail(email.value()).map(Mappers::toDomain);
    }

    @Override
    public Voter save(Voter voter) {
        var entity = Mappers.toEntity(voter);
        return Mappers.toDomain(jpa.save(entity));
    }
}

