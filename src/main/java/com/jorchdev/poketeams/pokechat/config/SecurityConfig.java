package com.jorchdev.poketeams.pokechat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configures web security to allow GraphQL requests with gateway-forwarded identity headers.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the reactive web application.
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/graphql", "/graphql/**").permitAll()
                        .anyExchange().permitAll()
                );
        return http.build();
    }
}
