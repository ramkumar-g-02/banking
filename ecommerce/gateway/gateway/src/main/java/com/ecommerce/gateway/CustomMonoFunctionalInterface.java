package com.ecommerce.gateway;

import exception.BussinessException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ShortcutConfigurable;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface CustomMonoFunctionalInterface<E extends Exception> extends ShortcutConfigurable {
    Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) throws E, BussinessException;

}
