package com.lokhit.voterservice.adapters.rest.vote;

public record CastVoteRequest(String voterId, String electionId, String optionId) { }
