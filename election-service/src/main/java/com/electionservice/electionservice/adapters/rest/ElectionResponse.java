package com.electionservice.electionservice.adapters.rest;

import com.electionservice.electionservice.domain.model.Election;

public record ElectionResponse(String id, String name, String status, String createdAt) {
    public static ElectionResponse from(Election e) {
        return new ElectionResponse(
                e.id().value().toString(),
                e.name(),
                e.status().name(),
                e.createdAt().toString()
        );
    }
}

