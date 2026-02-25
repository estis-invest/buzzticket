package com.efpcode.domain.model;

import java.util.UUID;
import java.time.Instant;

public record Ticket (UUID id, String title, TicketStatus status, Instant time) {

    public Ticket{
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be blank");
        if (status == null) throw new IllegalArgumentException("Status cannot be null");
        if(time == null) throw new IllegalArgumentException("Timestamp cannot be null");
    }

    public Ticket(String title){
        this(UUID.randomUUID(), title, TicketStatus.PENDING, Instant.now());
    }
}
