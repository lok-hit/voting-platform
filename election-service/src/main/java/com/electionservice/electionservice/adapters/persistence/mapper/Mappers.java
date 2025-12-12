package com.electionservice.electionservice.adapters.persistence.mapper;

import com.electionservice.electionservice.adapters.persistence.entity.ElectionEntity;
import com.electionservice.electionservice.adapters.persistence.entity.OptionEntity;
import com.electionservice.electionservice.domain.model.*;

import java.util.stream.Collectors;

public final class Mappers {

    // --- Election ---
    public static Election toDomain(ElectionEntity e) {
        var election = new Election(
                new ElectionId(e.getId()),
                e.getName(),
                ElectionStatus.valueOf(e.getStatus()),
                e.getCreatedAt()
        );

        // map options if loaded
        if (e.getOptions() != null) {
            var opts = e.getOptions().stream()
                    .map(Mappers::toDomain)
                    .toList();
            opts.forEach(election::addOption);
        }

        return election;
    }

    public static ElectionEntity toEntity(Election d) {
        var e = new ElectionEntity();
        e.setId(d.id().value());
        e.setName(d.name());
        e.setStatus(d.status().name());
        e.setCreatedAt(d.createdAt());

        // map options if present
        var opts = d.options().stream()
                .map(opt -> toEntity(opt, d.id()))
                .collect(Collectors.toList());
        e.setOptions(opts);

        return e;
    }

    // --- Option ---
    public static Option toDomain(OptionEntity e) {
        return new Option(new OptionId(e.getId()), e.getName());
    }

    public static OptionEntity toEntity(Option option, ElectionId electionId) {
        var e = new OptionEntity();
        e.setId(option.id().value());
        e.setName(option.name());
        e.setElectionId(electionId.value());
        return e;
    }

    private Mappers() { }
}
