package com.electionservice.electionservice.adapters.rest;


import com.electionservice.electionservice.domain.model.Option;

public record OptionResponse(String id, String name) {
    public static OptionResponse from(Option o) {
        return new OptionResponse(o.id().value().toString(), o.name());
    }
}

