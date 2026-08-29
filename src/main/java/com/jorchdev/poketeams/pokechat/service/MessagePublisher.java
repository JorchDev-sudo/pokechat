package com.jorchdev.poketeams.pokechat.service;

import com.jorchdev.poketeams.pokechat.domain.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessagePublisher {
    private final ConcurrentHashMap<UUID, Sinks.Many<Message>> references = new ConcurrentHashMap<>();

    public void publish(UUID conversationId, Message message) {
        manageSink(conversationId).tryEmitNext(message);

    }

    public Flux<Message> subscribe(UUID conversationId){
        return manageSink(conversationId).asFlux();

    }

    private Sinks.Many<Message> manageSink(UUID conversationId){
        return references.computeIfAbsent(conversationId, k -> Sinks.many().multicast().onBackpressureBuffer());
    }
}
