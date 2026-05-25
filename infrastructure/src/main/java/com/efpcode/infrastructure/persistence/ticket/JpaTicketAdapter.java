package com.efpcode.infrastructure.persistence.ticket;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.model.TicketSlug;
import com.efpcode.domain.ticket.port.TicketRepository;
import com.efpcode.domain.user.model.UserId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JpaTicketAdapter implements TicketRepository {

  private final SpringDataTicketRepository ticketRepository;

  public JpaTicketAdapter(SpringDataTicketRepository ticketRepository) {
    this.ticketRepository = ticketRepository;
  }

  @Override
  @Transactional
  public void save(Ticket ticket) {
    TicketEntity entity = TicketMapper.toEntity(ticket);
    ticketRepository.save(entity);
  }

  @Override
  public Optional<Ticket> findById(TicketId id) {
    return ticketRepository.findById(id.ticketId()).map(TicketMapper::toDomain);
  }

  @Override
  public Optional<Ticket> findBySlug(TicketSlug slug) {
    return ticketRepository.findBySlug(slug.slug()).map(TicketMapper::toDomain);
  }

  @Override
  public List<Ticket> findByReportedBy(UserId id) {
    return ticketRepository.findByReportedById(id.id()).stream()
        .map(TicketMapper::toDomain)
        .toList();
  }

  @Override
  public List<Ticket> findByAssigneeAndPartner(UserId id, PartnerId partnerId) {
    return ticketRepository.findByAssignee(id.id(), partnerId.partnerId()).stream()
        .map(TicketMapper::toDomain)
        .toList();
  }

  @Override
  public List<Ticket> findByOwnerPartner(PartnerId id) {
    return ticketRepository.findByOwnerPartnerId(id.partnerId()).stream()
        .map(TicketMapper::toDomain)
        .toList();
  }
}
