package com.electionservice.electionservice.adapters.persistence.repository;

import com.electionservice.electionservice.adapters.persistence.entity.OptionEntity;
import com.electionservice.electionservice.adapters.persistence.mapper.Mappers;
import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.model.Option;
import com.electionservice.electionservice.domain.port.OptionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaOptionRepository implements OptionRepository {
    private final SpringDataOptionJpa jpa;

    public JpaOptionRepository(SpringDataOptionJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public Option save(Option option, ElectionId electionId) {
        OptionEntity entity = Mappers.toEntity(option, electionId);
        OptionEntity saved = jpa.save(entity);
        return Mappers.toDomain(saved);
    }

    @Override
    public List<Option> findByElection(ElectionId electionId) {
        return jpa.findByElectionId(electionId.value()).stream()
                .map(Mappers::toDomain)
                .collect(Collectors.toList());
    }
}


