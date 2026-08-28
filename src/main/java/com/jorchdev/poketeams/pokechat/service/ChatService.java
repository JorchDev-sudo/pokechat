package com.jorchdev.poketeams.pokechat.service;

import com.jorchdev.poketeams.pokechat.domain.Conversation;
import com.jorchdev.poketeams.pokechat.repository.ConversationRepository;
import com.jorchdev.poketeams.pokechat.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private final ConversationRepository conversationRepository;

    public ChatService(ConversationRepository conversationRepository){
        this.conversationRepository = conversationRepository;
    }

    public Conversation initConversation(List<UUID> participantsIds){
        Conversation conversation = new Conversation(participantsIds);

        return conversation
    }
}
