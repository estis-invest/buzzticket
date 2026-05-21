package com.efpcode.application.usecase.ticket.dto;

import java.util.UUID;

public record ChangeTicketStatusCommand(String status, UUID ticketId) {}
