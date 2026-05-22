package com.efpcode.application.usecase.ticket.dto;

import java.util.UUID;

public record AssignTicketCommand(UUID ticketId, UUID assigneeId) {}
