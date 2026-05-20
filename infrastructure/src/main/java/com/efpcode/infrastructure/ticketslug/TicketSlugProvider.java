package com.efpcode.infrastructure.ticketslug;

import com.efpcode.application.port.out.ticket.TicketSlugGenerator;
import com.efpcode.domain.ticket.model.TicketSlug;
import com.efpcode.infrastructure.config.properties.TicketSlugProperties;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
class ConfigurableSlugTicketGenerator implements TicketSlugGenerator {

  private final TicketSlugProperties properties;
  private final AtomicLong counter = new AtomicLong(1);

  public ConfigurableSlugTicketGenerator(TicketSlugProperties properties) {
    this.properties = properties;
  }

  @Override
  public TicketSlug generate() {
    long number = counter.getAndIncrement();

    String ticketSlug =
        properties.prefix() + String.format("%0" + properties.lengthPadding() + "d", number);

    return new TicketSlug(ticketSlug);
  }
}
