package com.efpcode.domain.ticket.port;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.ticket.model.Ticket;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.model.TicketSlug;
import com.efpcode.domain.user.model.UserId;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {

  void save(Ticket ticket);

  Optional<Ticket> findById(TicketId id);

  Optional<Ticket> findBySlug(TicketSlug slug);

  List<Ticket> findByReportedBy(UserId id);

  List<Ticket> findByAssigneeAndPartner(UserId id, PartnerId partnerId);

  List<Ticket> findByOwnerPartner(PartnerId id);
}
