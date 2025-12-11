package com.lokhit.gatewayservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Integration tests for Gateway using Testcontainers (Redis) and WireMock.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@Testcontainers
class GatewayIntegrationTest {

    // Redis Testcontainer
    @Container
    private static final GenericContainer<?> REDIS = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379)
            .withReuse(true);

    // WireMock server - do NOT initialize statically to avoid early classloading of Jetty
    private static WireMockServer wireMock;

    @Autowired
    private WebTestClient webClient;

    /**
     * Sets up the test environment before any test methods are run.
     * Initializes and starts the WireMock server with a dynamic port.
     * This method is automatically called by JUnit before any test methods are executed.
     */
    @BeforeAll
    static void beforeAll() {
        // start WireMock here (lazy-init) so classloading occurs after test infrastructure is set up
        wireMock = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMock.start();
    }

    /**
     * Cleans up the test environment after all test methods have completed.
     * Stops the WireMock server if it's still running.
     * This method is automatically called by JUnit after all test methods have completed.
     */
    @AfterAll
    static void afterAll() {
        if (wireMock != null && wireMock.isRunning()) {
            wireMock.stop();
        }
    }

    /**
     * Registers dynamic properties for the test environment.
     * Configures the Redis host and port from the test container,
     * and makes the WireMock port available for test configuration.
     *
     * @param registry the DynamicPropertyRegistry to register properties with
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // Redis container connection for Spring
        registry.add("spring.redis.host", REDIS::getHost);
        registry.add("spring.redis.port", () -> REDIS.getMappedPort(6379));
        // WireMock port available for application-test.yml substitution
        registry.add("wiremock.port", () -> wireMock.port());
    }

    /**
     * Resets all WireMock stubs before each test method.
     * Ensures a clean state for each test by removing any existing stubs.
     * This method is automatically called by JUnit before each test method.
     */
    @BeforeEach
    void resetStubs() {
        wireMock.resetAll();
    }

    /**
     * Tests that the gateway correctly forwards requests to the downstream service
     * and adds an X-Request-Id header to the request.
     * Verifies:
     * - The request is forwarded to the correct downstream endpoint
     * - The response from the downstream service is returned correctly
     * - The X-Request-Id header is added to the downstream request
     */
    @Test
    void routingForwardsToDownstream_and_adds_request_id_header() {
        wireMock.stubFor(get(urlEqualTo("/ok"))
                .willReturn(aResponse().withStatus(200).withBody("downstream-ok")));

        webClient.get()
                .uri("/auth/ok")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("downstream-ok");

        wireMock.verify(getRequestedFor(urlEqualTo("/ok"))
                .withHeader("X-Request-Id", matching(".+")));
    }

    /**
     * Tests that the circuit breaker fallback is triggered when the downstream service
     * returns a server error (5xx).
     * Verifies:
     * - The fallback response is returned when the downstream service returns 500
     * - The response status is 503 (Service Unavailable) when fallback is used
     * - The response body matches the expected fallback message
     */
    @Test
    void fallbackIsUsed_whenDownstreamReturnsServerError() {
        wireMock.stubFor(get(urlEqualTo("/err"))
                .willReturn(aResponse().withStatus(500).withBody("boom")));

        webClient.get()
                .uri("/auth/err")
                .exchange()
                .expectStatus().isEqualTo(503)
                .expectBody(String.class)
                .value(body -> Assertions.assertTrue(body.contains("auth-service is unavailable")));
    }

    /**
     * Tests the rate limiting functionality of the gateway.
     * Verifies that when the rate limit is exceeded (more than 1 request per second),
     * subsequent requests receive a 429 (Too Many Requests) status code.
     * 
     * Note: This test assumes a rate limit of 1 request per second is configured.
     */
    @Test
    void deterministicRateLimiter_blocksSecondImmediateRequest_with429() {
        wireMock.stubFor(get(urlEqualTo("/rl"))
                .willReturn(aResponse().withStatus(200).withBody("ok")));

        // First request should be allowed (consumes the single token)
        webClient.get()
                .uri("/auth/rl")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("ok");

        // Immediate second request should be rate-limited -> 429 Too Many Requests
        webClient.get()
                .uri("/auth/rl")
                .exchange()
                .expectStatus().isEqualTo(429);
    }
}