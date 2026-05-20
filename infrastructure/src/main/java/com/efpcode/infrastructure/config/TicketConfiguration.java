package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.port.out.ticket.TicketSlugGenerator;
import com.efpcode.application.usecase.ticket.TicketCreationUseCase;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.domain.ticket.model.TicketId;
import com.efpcode.domain.ticket.port.TicketRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TicketConfiguration {

    @Bean
    public TicketCreationUseCase ticketCreationUseCase(
            IdGenerator<TicketId> ticketIdGenerator,
            TicketRepository ticketRepository,
            TicketSlugGenerator slugGenerator,
            UserAuthenticationPolicy userAuthenticationPolicy,
            PartnerRepository partnerRepository,
            Clock clock

    ){
        return new TicketCreationUseCase(ticketIdGenerator, ticketRepository, slugGenerator, userAuthenticationPolicy, partnerRepository, clock);
    }
}
