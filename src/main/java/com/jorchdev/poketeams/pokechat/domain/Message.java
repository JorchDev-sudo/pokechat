package com.jorchdev.poketeams.pokechat.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table(name = "messages")
@Getter
public class Message {
    @Id
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private Timestamp sentAt;

    public Message(UUID conversationId, UUID senderId, String content) {
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.content = content;

        this.sentAt = new Timestamp(System.currentTimeMillis());
    }
}
