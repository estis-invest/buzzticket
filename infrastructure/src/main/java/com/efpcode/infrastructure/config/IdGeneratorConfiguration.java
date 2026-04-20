package com.efpcode.infrastructure.config;

import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.infrastructure.id.JugIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IdGeneratorConfiguration {

  @Bean
  public IdGenerator<PartnerId> partnerIdIdGenerator() {
    return new JugIdGenerator<>(PartnerId::new);
  }
}
