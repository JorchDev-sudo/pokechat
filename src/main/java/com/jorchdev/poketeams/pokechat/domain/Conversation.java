package com.jorchdev.poketeams.pokechat.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Table(name = "conversations")
@Getter
public class Conversation {
    @Id
    private UUID id;
    private List<UUID> participantIds;
    private Timestamp createdAt;

    public Conversation(List<UUID> participantIds){
        this.participantIds = participantIds;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
