package com.electionservice.electionservice.adapters.persistence.repository;

import com.electionservice.electionservice.adapters.persistence.entity.ElectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataElectionJpa extends JpaRepository<ElectionEntity, UUID> { }

