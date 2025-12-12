package com.electionservice.electionservice.domain.event;

import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.model.OptionId;

public record OptionAdded(ElectionId electionId, OptionId optionId, String name) implements DomainEvent { }

