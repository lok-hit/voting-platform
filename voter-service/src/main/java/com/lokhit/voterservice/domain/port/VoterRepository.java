package com.lokhit.voterservice.domain.port;

import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.values.Email;

import java.util.Optional;

public interface VoterRepository {
    Optional<Voter> findById(VoterId id);
    Optional<Voter> findByEmail(Email email);
    Voter save(Voter voter);
}

