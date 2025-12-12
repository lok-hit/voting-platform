package com.electionservice.electionservice.application.command;

import com.electionservice.electionservice.domain.event.OptionAdded;
import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.model.Option;
import com.electionservice.electionservice.domain.port.DomainEventPublisher;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import com.electionservice.electionservice.domain.port.OptionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public final class AddOptionCommandHandler {
    private final ElectionRepository elections;
    private final OptionRepository options;
    private final DomainEventPublisher events;

    public AddOptionCommandHandler(ElectionRepository elections,
                                   OptionRepository options,
                                   DomainEventPublisher events) {
        this.elections = elections;
        this.options = options;
        this.events = events;
    }

    @Transactional
    public Option add(String electionIdRaw, String optionName) {
        var electionId = new ElectionId(UUID.fromString(electionIdRaw));
        var election = elections.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election not found"));

        var option = Option.create(optionName);
        election.addOption(option); // domain invariant: cannot add to closed election

        var savedOption = options.save(option, electionId);
        events.publish(new OptionAdded(election.id(), savedOption.id(), savedOption.name()));
        // persist election aggregate state if needed by your mapping (optional)
        elections.save(election);
        return savedOption;
    }
}

