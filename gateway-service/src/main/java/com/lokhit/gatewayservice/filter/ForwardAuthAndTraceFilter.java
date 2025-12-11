package com.lokhit.gatewayservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ForwardAuthAndTraceFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ForwardAuthAndTraceFilter.class);

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

    @Override
    public int getOrder() {
        return -1;
    }
}
