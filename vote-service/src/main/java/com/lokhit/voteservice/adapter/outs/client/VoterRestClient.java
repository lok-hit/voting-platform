package com.lokhit.voteservice.adapter.outs.client;

import com.lokhit.voteservice.domain.port.VoterClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class VoterRestClient implements VoterClientPort {

    private final WebClient webClient;

    public VoterRestClient(@Value("${clients.voter.base-url}") String baseUrl, WebClient.Builder builder) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public boolean isVoterAllowed(Long voterId) {
        try {
            // expected endpoint: GET /api/voters/{id} returning 200 and body with "blocked" flag
            return webClient.get()
                    .uri("/api/voters/{id}", voterId)
                    .retrieve()
                    .bodyToMono(VoterResponse.class)
                    .map(v -> !v.isBlocked())
                    .block();
        } catch (Exception e) {
            return false;
        }
    }

    // local DTO to map response
    public static class VoterResponse {
        private Long id;
        private boolean blocked;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public boolean isBlocked() { return blocked; }
        public void setBlocked(boolean blocked) { this.blocked = blocked; }
    }
}