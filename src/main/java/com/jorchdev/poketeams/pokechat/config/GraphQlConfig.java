package com.jorchdev.poketeams.pokechat.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQlConfig {

    @Bean
    RuntimeWiringConfigurer scalars() {
        return wiring -> wiring.scalar(ExtendedScalars.DateTime);
    }
}