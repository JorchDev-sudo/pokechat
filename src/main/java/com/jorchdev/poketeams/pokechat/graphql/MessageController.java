package com.jorchdev.poketeams.pokechat.graphql;

import com.jorchdev.poketeams.pokechat.domain.Message;
import com.jorchdev.poketeams.pokechat.security.TrainerIdInterceptor;
import com.jorchdev.poketeams.pokechat.service.ConversationService;
import com.jorchdev.poketeams.pokechat.service.MessageService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * GraphQL controller exposing chat operations for the gateway.
 */
@Controller
public class MessageController {
    private final MessageService messageService;
    private final ConversationService conversationService;

    private static final int DEFAULT_PAGE_SIZE = 30;
    private static final int MAX_PAGE_SIZE = 100;

    public MessageController(MessageService ms, ConversationService cs) {this.messageService = ms ; this.conversationService = cs;}

//    Returns all conversations for the authenticated caller. Currently bugged
//    @QueryMapping
//    public Flux<Conversation> findConversations(ServerWebExchange exchange) {
//        return trainerIdentityExtractor.getTrainerId(exchange)
//                .flatMapMany(messageService::getConversations);
//    }
    @QueryMapping
    public Flux<Message> findMessages(
            @Argument UUID conversationId,
            @Argument Integer limit,
            @Argument OffsetDateTime before,
            @ContextValue(TrainerIdInterceptor.CONTEXT_KEY) UUID callerId) {

        int pageSize = (limit == null || limit <= 0)
                ? DEFAULT_PAGE_SIZE
                : Math.min(limit, MAX_PAGE_SIZE);

        return conversationService.assertParticipant(conversationId, callerId)
                .thenMany(Flux.defer(() -> messageService.getMessages(conversationId, before, pageSize)));
    }
    @MutationMapping
    public Mono<Message> sendMessage(@Argument UUID conversationId,
                                     @Argument String content,
                                     @ContextValue(TrainerIdInterceptor.CONTEXT_KEY) UUID callerId)
    {
        return messageService.sendMessage(conversationId, callerId, content);
    }

    @SubscriptionMapping
    public Flux<Message> messageAdded(@Argument UUID conversationId,
                                      @ContextValue(TrainerIdInterceptor.CONTEXT_KEY) UUID callerId) {
        return messageService.subscribe(conversationId, callerId);
    }
}
