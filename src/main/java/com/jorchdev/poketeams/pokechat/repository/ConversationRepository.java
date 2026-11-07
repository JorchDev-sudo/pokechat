package com.jorchdev.poketeams.pokechat.repository;

import com.jorchdev.poketeams.pokechat.domain.Conversation;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ConversationRepository extends ReactiveCrudRepository<Conversation, UUID> {
    @Query("""
    SELECT c.* FROM conversations c
    JOIN conversation_participants p ON p.conversation_id = c.id
    WHERE p.trainer_id = :trainerId
    ORDER BY c.created_at DESC
    """)
    Flux<Conversation> findAllByParticipant(UUID trainerId);
}
