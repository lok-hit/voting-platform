package com.lokhit.gatewayservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/{service}")
    public ResponseEntity<String> fallbackGet(@PathVariable String service) {
        return ResponseEntity.status(503).body(service + " is unavailable - fallback response (GET)");
    }

    @PostMapping("/{service}")
    public ResponseEntity<String> fallbackPost(@PathVariable String service) {
        return ResponseEntity.status(503).body(service + " is unavailable - fallback response (POST)");
    }
}
