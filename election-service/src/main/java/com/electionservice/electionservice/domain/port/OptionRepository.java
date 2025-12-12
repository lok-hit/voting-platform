package com.electionservice.electionservice.domain.port;

import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.model.Option;
import com.electionservice.electionservice.domain.model.OptionId;

import java.util.List;
import java.util.Optional;

public interface OptionRepository {
    Option save(Option option, ElectionId electionId);
    List<Option> findByElection(ElectionId electionId);
    Optional<Option> findById(OptionId optionId);
    boolean existsById(OptionId optionId);
}

