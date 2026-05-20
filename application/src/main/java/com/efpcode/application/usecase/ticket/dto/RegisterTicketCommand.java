package com.efpcode.application.usecase.ticket.dto;

import java.util.UUID;

public record RegisterTicketCommand(
    String title, String Description, String priority, UUID partnerId) {}
