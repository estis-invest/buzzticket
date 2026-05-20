package com.efpcode.infrastructure.ticketslug;

import com.efpcode.application.port.out.ticket.TicketSlugGenerator;
import com.efpcode.domain.ticket.model.TicketSlug;
import com.efpcode.infrastructure.config.properties.TicketSlugProperties;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class ConfigurableSlugTicketGenerator implements TicketSlugGenerator {

  private final TicketSlugProperties properties;

  public ConfigurableSlugTicketGenerator(TicketSlugProperties properties) {
    this.properties = properties;
  }

  @Override
  public TicketSlug generate() {
    UUID randomString = UUID.randomUUID();
    String hex = hexaString(randomString).toUpperCase();

    int maxSuffixLength = 32 - 4;
    int length = Math.min(properties.lengthPadding(), maxSuffixLength);

    String hexPadding = hex.substring(0, length);

    String ticketSlug = properties.prefix() + "-" + hexPadding;
    return new TicketSlug(ticketSlug);
  }

  private String hexaString(UUID value) {
    return value.toString().replace("-", "");
  }
}
