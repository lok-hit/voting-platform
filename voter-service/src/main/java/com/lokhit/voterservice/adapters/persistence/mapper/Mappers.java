package com.lokhit.voterservice.adapters.persistence.mapper;

import com.lokhit.voterservice.adapters.persistence.entity.VoteEntity;
import com.lokhit.voterservice.adapters.persistence.entity.VoterEntity;
import com.lokhit.voterservice.domain.model.*;
import com.lokhit.voterservice.domain.values.ElectionId;
import com.lokhit.voterservice.domain.values.Email;
import com.lokhit.voterservice.domain.values.OptionId;

public final class Mappers {
    public static Voter toDomain(VoterEntity e) {
        return new Voter(new VoterId(e.getId()), new Email(e.getEmail()),
                VoterStatus.valueOf(e.getStatus()), e.getRegisteredAt());
    }

    public static VoterEntity toEntity(Voter d) {
        var e = new VoterEntity();
        e.setId(d.id().value());
        e.setEmail(d.email().value());
        e.setStatus(d.status().name());
        e.setRegisteredAt(d.registeredAt());
        return e;
    }

    public static Vote toDomain(VoteEntity e) {
        return new Vote(new VoteId(e.getId()), new VoterId(e.getVoterId()),
                new ElectionId(e.getElectionId()), new OptionId(e.getOptionId()), e.getCastAt());
    }

    public static VoteEntity toEntity(Vote v) {
        var e = new VoteEntity();
        e.setId(v.id().value());
        e.setVoterId(v.voterId().value());
        e.setElectionId(v.electionId().value());
        e.setOptionId(v.optionId().value());
        e.setCastAt(v.castAt());
        return e;
    }

    private Mappers() {}
}

