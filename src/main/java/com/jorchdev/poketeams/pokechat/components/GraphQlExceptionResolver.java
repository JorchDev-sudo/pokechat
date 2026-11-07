package com.jorchdev.poketeams.pokechat.components;

import com.jorchdev.poketeams.pokechat.exceptions.ForbiddenException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Component
public class GraphQlExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof BindException) {
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.BAD_REQUEST)
                    .message("Invalid argument: " + ex.getMessage())
                    .build();
        }
        if (ex instanceof ResponseStatusException || ex instanceof NoSuchElementException) {
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .build();
        }
        if (ex instanceof ForbiddenException) {
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.FORBIDDEN)
                    .message(ex.getMessage())
                    .build();
        }
        if (ex instanceof IllegalArgumentException) {
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.BAD_REQUEST)
                    .message(ex.getMessage())
                    .build();
        }
        return null;
    }
}