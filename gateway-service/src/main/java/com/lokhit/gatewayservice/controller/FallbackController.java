package com.lokhit.gatewayservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling fallback requests when the target service is unavailable.
 * Provides default responses in case of service unavailability issues.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Handles GET requests to a service that is currently unavailable.
     *
     * @param service the name of the service that is unavailable
     * @return ResponseEntity with status code 503 (Service Unavailable) and a message about service unavailability
     */
    @GetMapping("/{service}")
    public ResponseEntity<String> fallbackGet(@PathVariable String service) {
        return ResponseEntity.status(503).body(service + " is unavailable - fallback response (GET)");
    }

    /**
     * Handles POST requests to a service that is currently unavailable.
     *
     * @param service the name of the service that is unavailable
     * @return ResponseEntity with status code 503 (Service Unavailable) and a message about service unavailability
     */
    @PostMapping("/{service}")
    public ResponseEntity<String> fallbackPost(@PathVariable String service) {
        return ResponseEntity.status(503).body(service + " is unavailable - fallback response (POST)");
    }
}
