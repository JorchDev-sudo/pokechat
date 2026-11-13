package com.jorchdev.poketeams.pokechat.service;

import com.jorchdev.poketeams.pokechat.domain.Conversation;
import com.jorchdev.poketeams.pokechat.domain.ConversationParticipant;
import com.jorchdev.poketeams.pokechat.exceptions.ForbiddenException;
import com.jorchdev.poketeams.pokechat.repository.ConversationParticipantRepository;
import com.jorchdev.poketeams.pokechat.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;

    public ConversationService(
            ConversationRepository cr,
            ConversationParticipantRepository cpr)
    {
        this.conversationRepository = cr;
        this.participantRepository = cpr;
    }

    @Transactional
    public Mono<Conversation> createConversation(UUID ownerId, List<UUID> participantIds) {
        Set<UUID> all = new LinkedHashSet<>(participantIds);

        all.add(ownerId);

        if (all.size() < 2) {
            return Mono.error(new IllegalArgumentException("A conversation needs at least two distinct participants"));
        }

        return conversationRepository.save(new Conversation(ownerId))
                .flatMap(conv -> Flux.fromIterable(all)
                        .concatMap(t -> participantRepository.add(conv.getId(), t))
                        .then(Mono.just(conv)));
    }

    public Mono<Void> assertParticipant(UUID conversationId, UUID trainerId) {
        return participantRepository.isParticipant(conversationId, trainerId)
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new ForbiddenException("Authorization Denied")))
                .then();
    }

    public Flux<Conversation> getConversations(UUID trainerId) {
        return conversationRepository.findAllByParticipant(trainerId);
    }

    public Mono<Map<Conversation, List<UUID>>> participantsByConversation(List<Conversation> convs) {
        List<UUID> ids = convs.stream().map(Conversation::getId).toList();
        return participantRepository.findAllByConversationIdIn(ids)
                .collectMultimap(ConversationParticipant::conversationId, ConversationParticipant::trainerId)
                .map(byId -> convs.stream().collect(Collectors.toMap(
                        c -> c,
                        c -> List.copyOf(byId.getOrDefault(c.getId(), List.of())))));
    }
}
