package com.electionservice.electionservice.domain.port;

import com.electionservice.electionservice.domain.model.ElectionId;
import com.electionservice.electionservice.domain.model.Option;

import java.util.List;

public interface OptionRepository {
    Option save(Option option, ElectionId electionId);
    List<Option> findByElection(ElectionId electionId);
}

