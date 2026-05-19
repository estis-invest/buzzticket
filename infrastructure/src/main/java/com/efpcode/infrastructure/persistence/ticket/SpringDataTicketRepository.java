package com.efpcode.infrastructure.persistence.ticket;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTicketRepository extends JpaRepository<TicketEntity, UUID> {

  Optional<TicketEntity> findById(UUID id);

  Optional<TicketEntity> findBySlug(String slug);

  List<TicketEntity> findByReportedById(UUID reportedById);

  List<TicketEntity> findByAssignees(Set<UUID> assignees);

  List<TicketEntity> findByOwnerPartnerId(UUID ownerPartnerId);
}
