package com.lokhit.voterservice.adapters.persistence.repository;

import com.lokhit.voterservice.adapters.persistence.entity.VoterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataVoterJpa extends JpaRepository<VoterEntity, UUID> {
    Optional<VoterEntity> findByEmail(String email);
}

