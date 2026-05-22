package com.efpcode.application.usecase.ticket.dto;

import java.util.UUID;

public record ChangeTicketDescriptionCommand(String description, UUID ticketId) {}
