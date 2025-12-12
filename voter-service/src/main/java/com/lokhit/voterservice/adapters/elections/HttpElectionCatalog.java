package com.lokhit.voterservice.adapters.elections;

import com.lokhit.voterservice.domain.port.ElectionCatalog;
import com.lokhit.voterservice.domain.values.ElectionId;
import com.lokhit.voterservice.domain.values.OptionId;
import org.springframework.web.client.RestTemplate;

public class HttpElectionCatalog implements ElectionCatalog {
    private final RestTemplate http;
    private final String baseUrl;

    public HttpElectionCatalog(RestTemplate http, String baseUrl) {
        this.http = http;
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean electionExists(ElectionId electionId) {
        try {
            var resp = http.getForEntity(baseUrl + "/elections/" + electionId.value(), String.class);
            return resp.getStatusCode().is2xxSuccessful();
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    @Override
    public boolean optionExists(ElectionId electionId, OptionId optionId) {
        try {
            var resp = http.getForEntity(baseUrl + "/elections/" + electionId.value() + "/options/" + optionId.value(), String.class);
            return resp.getStatusCode().is2xxSuccessful();
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}

