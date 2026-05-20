package com.efpcode.application.usecase.ticket.dto;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerName;
import com.efpcode.domain.ticket.model.Ticket;

import java.time.Instant;
import java.util.UUID;

public record TicketResult(
        UUID ticketId,
        String slug,
        String title,
        String description,
        String status,
        String priority,
        String partnerName,
        Instant createdAt,
        Instant updatedAt

) {

    public static TicketResult fromDomain (Ticket ticket, PartnerName partnerName){
        return new TicketResult(
                ticket.id().ticketId(),
                ticket.slug().slug(),
                ticket.title().title(),
                ticket.description().description(),
                ticket.status().name(),
                ticket.priority().name(),
                partnerName.partnerName(),
                ticket.createdAt().time(),
                ticket.updatedAt().updatedAt()
        );
    }
}
