package com.jorchdev.poketeams.pokechat.domain;

import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("conversation_participants")
public record ConversationParticipant(UUID conversationId, UUID trainerId) {}
