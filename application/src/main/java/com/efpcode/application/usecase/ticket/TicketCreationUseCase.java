package com.efpcode.application.usecase.ticket;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.policy.user.dto.UserContext;
import com.efpcode.application.port.out.ticket.TicketSlugGenerator;
import com.efpcode.application.usecase.partner.exceptions.IllegalPartnerStatusException;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.application.usecase.ticket.dto.RegisterTicketCommand;
import com.efpcode.application.usecase.ticket.dto.TicketResult;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.ticket.model.*;
import com.efpcode.domain.ticket.port.TicketRepository;
import java.time.Clock;
import java.time.Instant;

public class TicketCreationUseCase {

  private final IdGenerator<TicketId> ticketIdGenerator;
  private final TicketRepository ticketRepository;
  private final TicketSlugGenerator slugGenerator;
  private final UserAuthenticationPolicy userAuthenticationPolicy;
  private final PartnerRepository partnerRepository;
  private final Clock clock;

  public TicketCreationUseCase(
      IdGenerator<TicketId> ticketIdGenerator,
      TicketRepository ticketRepository,
      TicketSlugGenerator slugGenerator,
      UserAuthenticationPolicy userAuthenticationPolicy,
      PartnerRepository partnerRepository,
      Clock clock) {
    this.ticketIdGenerator = ticketIdGenerator;
    this.ticketRepository = ticketRepository;
    this.slugGenerator = slugGenerator;
    this.userAuthenticationPolicy = userAuthenticationPolicy;
    this.partnerRepository = partnerRepository;
    this.clock = clock;
  }

  public TicketResult execute(RegisterTicketCommand command, RequestContext requestContext) {
    UserContext user = userAuthenticationPolicy.userValidator(requestContext);

    PartnerId partnerId = new PartnerId(command.partnerId());

    Partner partner =
        partnerRepository
            .findById(partnerId)
            .orElseThrow(
                () -> new PartnerNotFoundException("Partner not found and cannot generate ticket"));

    if (!partner.isActive()) {
      throw new IllegalPartnerStatusException(
          "Inactive partner cannot have association with ticket");
    }

    TicketId ticketId = ticketIdGenerator.generate();
    TicketSlug ticketSlug = slugGenerator.generate();
    Instant now = Instant.now(clock);

    TicketTitle ticketTitle = new TicketTitle(command.title());
    TicketDescription ticketDescription = new TicketDescription(command.description());
    TicketPriority ticketPriority = TicketPriorityParser.parse(command.priority());
    TicketCreatedAt createdAt = new TicketCreatedAt(now);

    Ticket ticket =
        Ticket.createPending(
            ticketId,
            ticketSlug,
            ticketTitle,
            ticketDescription,
            ticketPriority,
            createdAt,
            user.user().id(),
            partner.id());

    ticketRepository.save(ticket);

    return TicketResult.fromDomain(ticket, partner.name(), partnerId);
  }
}
