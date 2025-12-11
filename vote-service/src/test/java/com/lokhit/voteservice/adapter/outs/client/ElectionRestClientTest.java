package com.lokhit.voteservice.adapter.outs.client;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElectionRestClientTest {

    private static final String BASE_URL = "http://election-service";
    private static final Long ELECTION_ID = 1L;
    private static final Long OPTION_ID = 100L;

    @Mock
    private WebClient webClientMock;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;

    @Mock
    private WebClient.ResponseSpec responseMock;

    private ElectionRestClient electionRestClient;


    @BeforeEach
    void setUp() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClientMock);

        electionRestClient = new ElectionRestClient(BASE_URL, webClientBuilder);

        // Reset mocks before each test
        reset(webClientMock, requestHeadersUriMock, requestHeadersMock, responseMock);

        // Common mock setup
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
    }

    @Test
    void existsElection_WhenElectionExists_ShouldReturnTrue() {
        // Given
        when(requestHeadersUriMock.uri("/api/elections/{id}", ELECTION_ID)).thenReturn(requestHeadersMock);
        when(responseMock.toBodilessEntity())
                .thenReturn(Mono.just(org.springframework.http.ResponseEntity.ok().build()));

        // When
        boolean exists = electionRestClient.existsElection(ELECTION_ID);

        // Then
        assertTrue(exists);
        verify(webClientMock).get();
        verify(requestHeadersUriMock).uri("/api/elections/{id}", ELECTION_ID);
        verify(requestHeadersMock).retrieve();
        verify(responseMock).toBodilessEntity();
    }

    @Test
    void existsElection_WhenElectionDoesNotExist_ShouldReturnFalse() {
        // Given
        when(requestHeadersUriMock.uri("/api/elections/{id}", ELECTION_ID)).thenReturn(requestHeadersMock);
        when(responseMock.toBodilessEntity())
                .thenReturn(Mono.error(new RuntimeException("Test error")));

        // When
        boolean exists = electionRestClient.existsElection(ELECTION_ID);

        // Then
        assertFalse(exists);
        verify(webClientMock).get();
        verify(requestHeadersUriMock).uri("/api/elections/{id}", ELECTION_ID);
        verify(requestHeadersMock).retrieve();
        verify(responseMock).toBodilessEntity();
    }

    @Test
    void isOptionInElection_WhenOptionExists_ShouldReturnTrue() {
        // Given
        when(requestHeadersUriMock.uri("/api/elections/{electionId}/options/{optionId}", ELECTION_ID, OPTION_ID))
                .thenReturn(requestHeadersMock);
        when(responseMock.toBodilessEntity())
                .thenReturn(Mono.just(org.springframework.http.ResponseEntity.ok().build()));

        // When
        boolean exists = electionRestClient.isOptionInElection(ELECTION_ID, OPTION_ID);

        // Then
        assertTrue(exists);
        verify(webClientMock).get();
        verify(requestHeadersUriMock).uri("/api/elections/{electionId}/options/{optionId}", ELECTION_ID, OPTION_ID);
        verify(requestHeadersMock).retrieve();
        verify(responseMock).toBodilessEntity();
    }

    @Test
    void isOptionInElection_WhenOptionDoesNotExist_ShouldReturnFalse() {
        // Given
        when(requestHeadersUriMock.uri("/api/elections/{electionId}/options/{optionId}", ELECTION_ID, OPTION_ID))
                .thenReturn(requestHeadersMock);
        when(responseMock.toBodilessEntity())
                .thenReturn(Mono.error(new RuntimeException("Test error")));

        // When
        boolean exists = electionRestClient.isOptionInElection(ELECTION_ID, OPTION_ID);

        // Then
        assertFalse(exists);
        verify(webClientMock).get();
        verify(requestHeadersUriMock).uri("/api/elections/{electionId}/options/{optionId}", ELECTION_ID, OPTION_ID);
        verify(requestHeadersMock).retrieve();
        verify(responseMock).toBodilessEntity();
    }
}
