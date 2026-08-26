package com.jorchdev.poketeams.pokechat.repositories;

import com.jorchdev.poketeams.pokechat.model.Message;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface MessageRepository extends ReactiveCrudRepository<Message, UUID> {
    Flux<Message> findMessagesByConversationId(UUID conversationId);
}
