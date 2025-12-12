package com.electionservice.electionservice.application.query;

import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.model.Option;
import com.electionservice.electionservice.domain.model.OptionId;
import com.electionservice.electionservice.domain.port.OptionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class OptionQueryService {
    private final OptionRepository options;

    public OptionQueryService(OptionRepository options) {
        this.options = options;
    }

    public List<Option> listByElection(String electionIdRaw) {
        var id = new ElectionId(UUID.fromString(electionIdRaw));
        return options.findByElection(id);
    }

    public Optional<Option> findById(OptionId optionId) {
        return options.findById(optionId);
    }
}

