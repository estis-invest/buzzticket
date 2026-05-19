package com.efpcode.infrastructure.persistence.ticket;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.model.*;
import com.efpcode.domain.user.model.UserId;

import java.util.HashSet;
import java.util.stream.Collectors;

public class TicketMapper {
    private TicketMapper(){}


    public static TicketEntity toEntity(Ticket domain){

        TicketEntity entity = new TicketEntity();

        entity.setId(domain.id().ticketId());
        entity.setSlug(domain.slug().slug());
        entity.setTitle(domain.title().title());
        entity.setDescription(domain.description().description());
        entity.setStatus(domain.status().name());
        entity.setPriority(domain.priority().name());
        entity.setCreatedAt(domain.createdAt().time());
        entity.setUpdatedAt(domain.updatedAt().updatedAt());
        entity.setAssignees(domain.workers()
                .workers()
                .stream()
                .map(UserId::id)
                .collect(Collectors.toCollection(HashSet::new))
        );

        entity.setReportedById(domain.reportedBy().id());
        entity.setOwnerPartnerId(domain.ownerPartner().partnerId());

        return entity;

    }


    public static Ticket toDomain(TicketEntity entity){

        var assignees = entity.getAssignees();

        TicketAssignees domainAssignees =
                (assignees == null || assignees.isEmpty())
                        ? TicketAssignees.empty()
                        : new TicketAssignees(
                        assignees.stream()
                                .map(UserId::new)
                                .collect(Collectors.toSet())
                );


        return new Ticket(
                new TicketId(entity.getId()),
                new TicketSlug(entity.getSlug()),
                new TicketTitle(entity.getTitle()),
                new TicketDescription(entity.getDescription()),
                TicketStatus.valueOf(entity.getStatus()),
                TicketPriority.valueOf(entity.getPriority()),
                TicketCreatedAt.of(entity.getCreatedAt()),
                TicketUpdateAt.of(entity.getUpdatedAt()),
                domainAssignees,
                new UserId(entity.getReportedById()),
                new PartnerId(entity.getOwnerPartnerId())

        );
    }

}
