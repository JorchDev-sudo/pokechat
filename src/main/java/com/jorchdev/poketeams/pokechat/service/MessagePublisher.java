package com.jorchdev.poketeams.pokechat.service;

import com.jorchdev.poketeams.pokechat.domain.Message;
import com.jorchdev.poketeams.pokechat.repository.MessageRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MessagePublisher {
    private final MessageRepository messageRepository;

    public MessagePublisher(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Flux<Message> send
}
