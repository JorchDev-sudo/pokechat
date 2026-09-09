package com.jorchdev.poketeams.pokechat.service;

import com.jorchdev.poketeams.pokechat.domain.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessagePublisher {
    private final ConcurrentHashMap<UUID, Sinks.Many<Message>> references = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, AtomicInteger> subscriberCount = new ConcurrentHashMap<>();

    public void publish(UUID conversationId, Message message) {
        if (references.containsKey(conversationId)) {
            Sinks.Many<Message> sink = references.get(conversationId);
            sink.tryEmitNext(message);
        }
    }

    public Flux<Message> subscribe(UUID conversationId){
        return manageSink(conversationId).asFlux().doFinally(s -> manageUnsubscribe(conversationId));

    }

    private Sinks.Many<Message> manageSink(UUID conversationId){
        subscriberCount.computeIfAbsent(conversationId, k -> new AtomicInteger()).incrementAndGet();

        return references.computeIfAbsent(conversationId, k -> Sinks.many().multicast().onBackpressureBuffer());
    }

    private void manageUnsubscribe(UUID conversationId){
        AtomicInteger count = subscriberCount.get(conversationId);

        if (count != null && count.decrementAndGet() == 0) {
            subscriberCount.remove(conversationId);

            references.remove(conversationId);
        }
    }
}
