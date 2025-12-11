package com.lokhit.voteservice.adapter.outs.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoterRestClientTest {

    private static final String BASE_URL = "http://voter-service";
    private static final Long VOTER_ID = 1L;

    @Mock
    private WebClient webClientMock;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;

    @Mock
    private WebClient.ResponseSpec responseMock;

    private VoterRestClient voterRestClient;

    @BeforeEach
    void setUp() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClientMock);

        voterRestClient = new VoterRestClient(BASE_URL, webClientBuilder);

        // Common mock setup
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(anyString(), any(Object.class))).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
    }

    @Test
    void isVoterAllowed_WhenVoterIsNotBlocked_ShouldReturnTrue() {
        // Given
        VoterRestClient.VoterResponse voterResponse = new VoterRestClient.VoterResponse();
        voterResponse.setId(VOTER_ID);
        voterResponse.setBlocked(false);

        when(responseMock.bodyToMono(VoterRestClient.VoterResponse.class))
                .thenReturn(Mono.just(voterResponse));

        // When
        boolean isAllowed = voterRestClient.isVoterAllowed(VOTER_ID);

        // Then
        assertTrue(isAllowed);
        verify(webClientMock).get();
        verify(requestHeadersUriMock).uri("/api/voters/{id}", VOTER_ID);
        verify(requestHeadersMock).retrieve();
        verify(responseMock).bodyToMono(VoterRestClient.VoterResponse.class);
    }

    @Test
    void isVoterAllowed_WhenVoterIsBlocked_ShouldReturnFalse() {
        // Given
        VoterRestClient.VoterResponse voterResponse = new VoterRestClient.VoterResponse();
        voterResponse.setId(VOTER_ID);
        voterResponse.setBlocked(true);

        when(responseMock.bodyToMono(VoterRestClient.VoterResponse.class))
                .thenReturn(Mono.just(voterResponse));

        // When
        boolean isAllowed = voterRestClient.isVoterAllowed(VOTER_ID);

        // Then
        assertFalse(isAllowed);
        verify(webClientMock).get();
        verify(requestHeadersUriMock).uri("/api/voters/{id}", VOTER_ID);
        verify(requestHeadersMock).retrieve();
        verify(responseMock).bodyToMono(VoterRestClient.VoterResponse.class);
    }

    @Test
    void isVoterAllowed_WhenErrorOccurs_ShouldReturnFalse() {
        // Given
        when(responseMock.bodyToMono(VoterRestClient.VoterResponse.class))
                .thenReturn(Mono.error(new RuntimeException("Test error")));

        // When
        boolean isAllowed = voterRestClient.isVoterAllowed(VOTER_ID);

        // Then
        assertFalse(isAllowed);
        verify(webClientMock).get();
        verify(requestHeadersUriMock).uri("/api/voters/{id}", VOTER_ID);
        verify(requestHeadersMock).retrieve();
        verify(responseMock).bodyToMono(VoterRestClient.VoterResponse.class);
    }
}
