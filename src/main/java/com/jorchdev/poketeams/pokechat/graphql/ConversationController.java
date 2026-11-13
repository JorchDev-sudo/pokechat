package com.jorchdev.poketeams.pokechat.graphql;

import com.jorchdev.poketeams.pokechat.domain.Conversation;
import com.jorchdev.poketeams.pokechat.security.TrainerIdInterceptor;
import com.jorchdev.poketeams.pokechat.service.ConversationService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {this.conversationService = conversationService;}

    @MutationMapping
    public Mono<Conversation> startConversation(
            @Argument List<UUID> participantIds,
            @ContextValue(TrainerIdInterceptor.CONTEXT_KEY) UUID currentTrainerId) {

        return conversationService.createConversation(currentTrainerId, participantIds);
    }

    @QueryMapping
    public Flux<Conversation> findConversations(
            @ContextValue(TrainerIdInterceptor.CONTEXT_KEY) UUID callerId) {
        return conversationService.getConversations(callerId);
    }

    @BatchMapping(typeName = "Conversation", field = "participantIds")
    public Mono<Map<Conversation, List<UUID>>> participantIds(List<Conversation> conversations) {
        return conversationService.participantsByConversation(conversations);
    }
}
