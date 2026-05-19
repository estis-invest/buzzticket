package com.efpcode.infrastructure.persistence.ticket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataTicketRepository extends JpaRepository<TicketEntity, UUID> {

  Optional<TicketEntity> findBySlug(String slug);

  List<TicketEntity> findByReportedById(UUID reportedById);

  List<TicketEntity> findByOwnerPartnerId(UUID ownerPartnerId);

  @Query(
"""
    SELECT t FROM TicketEntity t
    JOIN t.assignees a
    WHERE a = :assigneeId
""")
  List<TicketEntity> findByAssignee(@Param("assigneeId") UUID assigneeId);
}
