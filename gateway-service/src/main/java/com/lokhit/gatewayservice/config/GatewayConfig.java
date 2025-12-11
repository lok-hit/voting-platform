package com.lokhit.gatewayservice.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * Configuration class for Spring Cloud Gateway specific beans and settings.
 * This class provides rate limiting configuration and other gateway-related beans.
 */
@Configuration
public class GatewayConfig {

    /**
     * Creates and configures a {@link KeyResolver} bean for rate limiting.
     * This resolver uses the client's IP address as the key for rate limiting,
     * allowing different rate limits to be applied per IP address.
     *
     * @return A {@link KeyResolver} that resolves to the client's IP address
     * @see KeyResolver
     */
    @Bean(name = "ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(getRemoteAddress(exchange));
    }

    /**
     * Extracts the remote IP address from the given {@link ServerWebExchange}.
     * Returns "unknown" if the remote address cannot be determined.
     *
     * @param exchange The server web exchange containing the client request
     * @return The client's IP address as a String, or "unknown" if not available
     */
    private String getRemoteAddress(ServerWebExchange exchange) {
        InetSocketAddress addr = exchange.getRequest().getRemoteAddress();
        if (addr == null) return "unknown";
        return addr.getAddress().getHostAddress();
    }
}
