package com.lokhit.voterservice.domain.port;


import com.lokhit.voterservice.domain.values.ElectionId;
import com.lokhit.voterservice.domain.values.OptionId;

public interface ElectionCatalog {
    boolean electionExists(ElectionId electionId);
    boolean optionExists(ElectionId electionId, OptionId optionId);
}

