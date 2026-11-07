package com.jorchdev.poketeams.pokechat.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Table("conversations")
@Getter
public class Conversation {
    @Id
    private UUID id;
    private final UUID ownerId;
    private final OffsetDateTime createdAt;

    public Conversation(UUID ownerId) {
        this(null, ownerId, OffsetDateTime.now());
    }

    @PersistenceCreator
    Conversation(UUID id, UUID ownerId, OffsetDateTime createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
    }
}