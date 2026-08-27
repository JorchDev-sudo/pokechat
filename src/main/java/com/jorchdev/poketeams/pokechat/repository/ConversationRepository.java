package com.jorchdev.poketeams.pokechat.repository;

import com.jorchdev.poketeams.pokechat.domain.Conversation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ConversationRepository extends ReactiveCrudRepository<Conversation, UUID> {
    //Todo Seguramente tenga que hacer una query específica para esta implementación
    Flux<Conversation> findAllByParticipantId(UUID participantId);
}
