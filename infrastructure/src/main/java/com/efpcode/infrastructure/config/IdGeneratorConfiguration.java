package com.efpcode.infrastructure.config;

import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.infrastructure.id.JugIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IdGeneratorConfiguration {

  @Bean
  public IdGenerator<PartnerId> partnerIdIdGenerator() {
    return new JugIdGenerator<>(PartnerId::new);
  }

  @Bean
  public IdGenerator<UserId> userIdIdGenerator() {
    return new JugIdGenerator<>(UserId::new);
  }

  @Bean
  public IdGenerator<StaffInvitationId> staffInvitationIdIdGenerator() {
    return new JugIdGenerator<>(StaffInvitationId::new);
  }

  @Bean
  public IdGenerator<TicketId> ticketIdIdGenerator() {
    return new JugIdGenerator<>(TicketId::new);
  }
}
