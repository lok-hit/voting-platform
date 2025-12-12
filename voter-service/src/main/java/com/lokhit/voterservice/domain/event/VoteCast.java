package com.lokhit.voterservice.domain.event;

import com.lokhit.voterservice.domain.model.VoteId;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.values.ElectionId;
import com.lokhit.voterservice.domain.values.OptionId;

public record VoteCast(VoteId voteId, VoterId voterId,
                       ElectionId electionId, OptionId optionId) implements DomainEvent { }
