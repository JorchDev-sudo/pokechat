package com.jorchdev.poketeams.pokechat.service;

import com.jorchdev.poketeams.pokechat.domain.Conversation;
import com.jorchdev.poketeams.pokechat.domain.Message;
import com.jorchdev.poketeams.pokechat.exceptions.AuthorizationException;
import com.jorchdev.poketeams.pokechat.repository.ConversationRepository;
import com.jorchdev.poketeams.pokechat.repository.MessageRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ChatService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessagePublisher messagePublisher;

    public ChatService(ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       MessagePublisher messagePublisher)
    {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.messagePublisher = messagePublisher;
    }

    public Mono<Message> sendMessage(UUID conversationId, UUID senderId, String content){
        Message newMessage = new Message(conversationId, senderId, content);

        Mono<Message> savedMessage = messageRepository.save(newMessage).doOnNext
                (message ->
                {
                    messagePublisher.publish(conversationId, message);
                }
                );

        return savedMessage;
    }

    public Flux<Message> subscribeToConversation(UUID participantId, UUID conversationId){
        Flux<Message> result =  conversationRepository.findById(conversationId).flatMapMany(
                (conversation) ->
                {
                    if (!conversation.getParticipantIds().contains(participantId)) {
                        return Flux.error(new AuthorizationException("Authorization Denied"));
                    }

                    return messagePublisher.subscribe(conversation.getId());
                }
        );

        return result;
    }

    public Flux<Message> getMessages(UUID conversationId, Pageable pageable){
        return messageRepository.findAllByConversationId(conversationId, pageable);
    }

    public Flux<Conversation> getConversations(UUID participantId){
        return conversationRepository.findAllByParticipantId(participantId);
    }
}
