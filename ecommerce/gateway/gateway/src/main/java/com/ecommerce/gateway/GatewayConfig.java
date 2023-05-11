//package com.ecommerce.gateway;
//
//import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayConfig {
//    @Bean
//    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route(predicateSpec -> predicateSpec.path("/cart/**").uri("lb://cart-service"))
//                .route(predicateSpec -> predicateSpec.path("/user/**").uri("lb://user-service"))
//                .route(predicateSpec -> predicateSpec.path("/product/**").uri("lb://product-service"))
//                .route(predicateSpec -> predicateSpec.path("/order/**").uri("lb://order-service"))
//                .build();
//    }
//
//}
