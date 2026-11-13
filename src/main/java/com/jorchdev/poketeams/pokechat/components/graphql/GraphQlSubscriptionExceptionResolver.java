package com.jorchdev.poketeams.pokechat.components.graphql;

import com.jorchdev.poketeams.pokechat.exceptions.ForbiddenException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.apache.logging.log4j.util.InternalException;
import org.jspecify.annotations.NonNull;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.execution.SubscriptionExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GraphQlSubscriptionExceptionResolver extends SubscriptionExceptionResolverAdapter {

    @Override
    protected List<GraphQLError> resolveToMultipleErrors(@NonNull Throwable ex) {
        if (ex instanceof ForbiddenException) {
            return List.of(GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.FORBIDDEN)
                    .message(ex.getMessage())
                    .build());
        }else if (ex instanceof InternalException) {
            return List.of(GraphqlErrorBuilder.newError()
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .message(ex.getMessage())
                    .build());
        }
        return null;
    }
}