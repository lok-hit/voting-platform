package com.lokhit.voteservice.application.dto;


import jakarta.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull
    private Long voterId;
    @NotNull
    private Long optionId;

    public Long getVoterId() { return voterId; }
    public void setVoterId(Long voterId) { this.voterId = voterId; }

    public Long getOptionId() { return optionId; }
    public void setOptionId(Long optionId) { this.optionId = optionId; }
}
