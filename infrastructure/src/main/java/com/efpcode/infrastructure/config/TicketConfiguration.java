package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.staff.StaffActionPolicy;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.port.out.ticket.TicketSlugGenerator;
import com.efpcode.application.usecase.ticket.*;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.port.TicketRepository;
import com.efpcode.domain.user.port.UserRepository;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketConfiguration {

  @Bean
  public TicketCreationUseCase ticketCreationUseCase(
      IdGenerator<TicketId> ticketIdGenerator,
      TicketRepository ticketRepository,
      TicketSlugGenerator slugGenerator,
      UserAuthenticationPolicy userAuthenticationPolicy,
      PartnerRepository partnerRepository,
      Clock clock) {

    return new TicketCreationUseCase(
        ticketIdGenerator,
        ticketRepository,
        slugGenerator,
        userAuthenticationPolicy,
        partnerRepository,
        clock);
  }

  @Bean
  public AssignTicketUseCase assignTicketUseCase(
      TicketRepository ticketRepository,
      UserRepository userRepository,
      StaffActionPolicy staffActionPolicy,
      Clock clock) {
    return new AssignTicketUseCase(ticketRepository, userRepository, staffActionPolicy, clock);
  }

  @Bean
  public UnassignTicketUseCase unassignTicketUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy, Clock clock) {
    return new UnassignTicketUseCase(ticketRepository, staffActionPolicy, clock);
  }

  @Bean
  public ChangeTicketStatusUseCase changeTicketStatusUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy, Clock clock) {
    return new ChangeTicketStatusUseCase(ticketRepository, staffActionPolicy, clock);
  }

  @Bean
  public ChangeTicketPriorityUseCase changeTicketPriorityUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy, Clock clock) {
    return new ChangeTicketPriorityUseCase(ticketRepository, staffActionPolicy, clock);
  }

  @Bean
  public ChangeTicketDescriptionUseCase changeTicketDescriptionUseCase(
      TicketRepository ticketRepository, StaffActionPolicy staffActionPolicy, Clock clock) {
    return new ChangeTicketDescriptionUseCase(ticketRepository, staffActionPolicy, clock);
  }
}
