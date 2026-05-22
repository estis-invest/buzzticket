package com.efpcode.application.usecase.ticket.dto;

import java.util.UUID;

public record UnassignTicketCommand(UUID ticketId, UUID assigneeId) {}
