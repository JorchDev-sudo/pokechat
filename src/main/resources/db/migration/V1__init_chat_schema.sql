create extension if not exists pgcrypto;

create table conversations (
                               id uuid primary key default gen_random_uuid(),
                               created_at timestamptz not null default now()
);

create table conversation_participants (
                                           conversation_id uuid not null references conversations(id) on delete cascade,
                                           trainer_id uuid not null,
                                           primary key (conversation_id, trainer_id)
);

create table messages (
                          id uuid primary key default gen_random_uuid(),
                          conversation_id uuid not null references conversations(id) on delete cascade,
                          sender_id uuid not null,
                          content text not null,
                          sent_at timestamptz not null default now()
);

create index idx_messages_conversation_sent_at
    on messages (conversation_id, sent_at desc);

create index idx_participants_trainer
    on conversation_participants (trainer_id);