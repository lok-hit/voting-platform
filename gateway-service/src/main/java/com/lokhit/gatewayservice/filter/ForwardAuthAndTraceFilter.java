package com.lokhit.gatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter responsible for adding authentication and request tracing headers.
 * Implements GlobalFilter and Ordered interfaces to enable global functionality in Spring Cloud Gateway.
 */
@Component
public class ForwardAuthAndTraceFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ForwardAuthAndTraceFilter.class);

    /**
     * Filter method that adds necessary headers to the request.
     * 
     * @param exchange ServerWebExchange object containing request and response information
     * @param chain GatewayFilterChain object for continuing the request processing
     * @return Mono<Void> reactive stream result
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        log.debug("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

        if (headers.getFirst("X-Forwarded-For") == null || headers.getFirst("X-Forwarded-For").isBlank()) {
            var remote = exchange.getRequest().getRemoteAddress();
            if (remote != null) {
                exchange = exchange.mutate()
                        .request(r -> r.header("X-Forwarded-For", remote.getAddress().getHostAddress()))
                        .build();
            }
        }

        exchange = exchange.mutate()
                .request(r -> r.header("X-Request-Id", java.util.UUID.randomUUID().toString()))
                .build();

        return chain.filter(exchange);
    }

    /**
     * Specifies the order of filter execution in the filter chain.
     * 
     * @return The order value of the filter (lower values have higher priority)
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
