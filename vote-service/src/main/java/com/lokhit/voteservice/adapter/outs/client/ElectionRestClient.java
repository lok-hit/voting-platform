package com.lokhit.voteservice.adapter.outs.client;

import com.lokhit.voteservice.domain.port.ElectionClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ElectionRestClient implements ElectionClientPort {

    private final WebClient webClient;

    public ElectionRestClient(@Value("${clients.election.base-url}") String baseUrl, WebClient.Builder builder) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public boolean existsElection(Long electionId) {
        try {
            return webClient.get()
                    .uri("/api/elections/{id}", electionId)
                    .retrieve()
                    .toBodilessEntity()
                    .map(resp -> resp.getStatusCode().is2xxSuccessful())
                    .defaultIfEmpty(false)
                    .block();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isOptionInElection(Long electionId, Long optionId) {
        try {
            return webClient.get()
                    .uri("/api/elections/{electionId}/options/{optionId}", electionId, optionId)
                    .retrieve()
                    .toBodilessEntity()
                    .map(ResponseEntity::getStatusCode)
                    .map(status -> status.is2xxSuccessful())
                    .defaultIfEmpty(false)
                    .block();
        } catch (Exception e) {
            return false;
        }
    }
}
