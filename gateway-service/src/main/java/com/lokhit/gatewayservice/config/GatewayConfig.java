package com.lokhit.gatewayservice.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Configuration
public class GatewayConfig {

    @Bean(name = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(getRemoteAddress(exchange));
    }

    private String getRemoteAddress(ServerWebExchange exchange) {
        InetSocketAddress addr = exchange.getRequest().getRemoteAddress();
        if (addr == null) return "unknown";
        return addr.getAddress().getHostAddress();
    }
}
