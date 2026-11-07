package com.jorchdev.poketeams.pokechat.repository;

import com.jorchdev.poketeams.pokechat.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface MessageRepository extends ReactiveCrudRepository<Message, UUID> {
    Flux<Message> findAllByConversationId(UUID conversationId, Pageable pageable);

    @Query("""
    SELECT * FROM messages
    WHERE conversation_id = :conversationId
    ORDER BY sent_at DESC
    LIMIT :limit
    """)
    Flux<Message> findLatest(UUID conversationId, int limit);

    @Query("""
    SELECT * FROM messages
    WHERE conversation_id = :conversationId
      AND sent_at < :before
    ORDER BY sent_at DESC
    LIMIT :limit
    """)
    Flux<Message> findBefore(UUID conversationId, OffsetDateTime before, int limit);
}
