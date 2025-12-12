package com.lokhit.voterservice.domain.values;


import java.time.Instant;

public interface TimeSource {
    Instant now();
}

