package com.jorchdev.poketeams.pokechat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Table
public class Conversation {
    @Id
    private UUID id;
    private List<UUID> participantIds;
    private Timestamp createdAt;
}
