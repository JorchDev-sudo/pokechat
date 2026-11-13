package com.jorchdev.poketeams.pokechat.service;

import com.jorchdev.poketeams.pokechat.domain.Message;
import com.jorchdev.poketeams.pokechat.exceptions.ForbiddenException;
import com.jorchdev.poketeams.pokechat.repository.ConversationRepository;
import com.jorchdev.poketeams.pokechat.repository.MessageRepository;
import com.jorchdev.poketeams.pokechat.service.publisher.MessagePublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class MessageService {
    private final ConversationService conversationService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessagePublisher messagePublisher;

    public MessageService(ConversationService cs, ConversationRepository cr, MessageRepository mr, MessagePublisher mp)
    {
        this.conversationService = cs;
        this.conversationRepository = cr;
        this.messageRepository = mr;
        this.messagePublisher = mp;
    }

    public Flux<Message> subscribe(UUID conversationId, UUID callerId) {
        return conversationService.assertParticipant(conversationId, callerId)
                .thenMany(Flux.defer(() -> messagePublisher.subscribe(conversationId)));
    }

    public Mono<Message> sendMessage(UUID conversationId, UUID senderId, String content) {
        return conversationService.assertParticipant(conversationId, senderId)
                .then(Mono.defer(() -> messageRepository.save(new Message(conversationId, senderId, content))))
                .doOnNext(m -> messagePublisher.publish(conversationId, m));
    }

    public Flux<Message> getMessages(UUID conversationId, OffsetDateTime before, int limit) {
        return before == null
                ? messageRepository.findLatest(conversationId, limit)
                : messageRepository.findBefore(conversationId, before, limit);
    }
}
