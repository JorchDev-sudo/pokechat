package com.jorchdev.poketeams.pokechat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table
public class Message {
    @Id
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String content;
    private Timestamp sentAt;
}
