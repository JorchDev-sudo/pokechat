package com.jorchdev.poketeams.pokechat.security;

import org.jspecify.annotations.NonNull;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Extracts the authenticated trainer identity from request headers forwarded by the gateway.
 */
@Component
public class TrainerIdInterceptor implements WebGraphQlInterceptor {

    public static final String HEADER = "X-Trainer-Id";
    public static final String CONTEXT_KEY = "currentTrainerId";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, @NonNull Chain chain) {
        String raw = request.getHeaders().getFirst(HEADER);

        if (raw != null) {
            UUID trainerId;
            try {
                trainerId = UUID.fromString(raw);
            } catch (IllegalArgumentException e) {
                return Mono.error(new IllegalArgumentException("Invalid " + HEADER + " header"));
            }
            request.configureExecutionInput((input, builder) ->
                    builder.graphQLContext(ctx -> ctx.put(CONTEXT_KEY, trainerId)).build());
        }
        return chain.next(request);
    }
}