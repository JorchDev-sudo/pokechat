package com.jorchdev.poketeams.pokechat.repository;

import com.jorchdev.poketeams.pokechat.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface MessageRepository extends ReactiveCrudRepository<Message, UUID> {
    Flux<Message> findAllBy(Pageable pageable);
}
