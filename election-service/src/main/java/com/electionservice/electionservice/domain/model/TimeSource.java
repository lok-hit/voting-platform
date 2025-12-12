package com.electionservice.electionservice.domain.model;


import java.time.Instant;

public interface TimeSource {
    Instant now();
}

