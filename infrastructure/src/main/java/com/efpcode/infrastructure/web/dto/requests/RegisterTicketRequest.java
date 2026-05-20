package com.efpcode.infrastructure.web.dto.requests;

import java.util.UUID;

public record RegisterTicketRequest(
    String title, String description, String priority, UUID partnerId) {}
