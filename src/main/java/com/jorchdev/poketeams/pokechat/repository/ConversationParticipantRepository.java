package com.jorchdev.poketeams.pokechat.repository;

import com.jorchdev.poketeams.pokechat.domain.ConversationParticipant;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;

public interface ConversationParticipantRepository extends ReactiveCrudRepository<ConversationParticipant, UUID> {
    @Query("""
    SELECT EXISTS (
        SELECT 1 FROM conversation_participants
        WHERE conversation_id = :conversationId
          AND trainer_id = :trainerId
    )
    """)
    Mono<Boolean> isParticipant(UUID conversationId, UUID trainerId);

    // ParticipantRepository
    @Modifying
    @Query("INSERT INTO conversation_participants (conversation_id, trainer_id) VALUES (:conversationId, :trainerId)")
    Mono<Void> add(UUID conversationId, UUID trainerId);

    Flux<ConversationParticipant> findAllByConversationIdIn(Collection<UUID> ids);
}
