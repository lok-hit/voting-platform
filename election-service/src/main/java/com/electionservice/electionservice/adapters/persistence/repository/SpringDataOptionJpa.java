package com.electionservice.electionservice.adapters.persistence.repository;

import com.electionservice.electionservice.adapters.persistence.entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataOptionJpa extends JpaRepository<OptionEntity, UUID> {
    List<OptionEntity> findByElectionId(UUID electionId);
}

