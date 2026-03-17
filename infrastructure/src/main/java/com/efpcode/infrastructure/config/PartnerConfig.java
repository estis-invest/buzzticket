package com.efpcode.infrastructure.config;

import com.efpcode.application.usecase.partner.GetPartnerUseCase;
import com.efpcode.application.usecase.partner.RegisterPartnerUseCase;
import com.efpcode.domain.partner.port.PartnerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PartnerConfig {

  @Bean
  public RegisterPartnerUseCase registerPartnerUseCase(PartnerRepository partnerRepository) {
    return new RegisterPartnerUseCase(partnerRepository);
  }

  @Bean
  public GetPartnerUseCase getPartnerUseCase(PartnerRepository partnerRepository) {
    return new GetPartnerUseCase(partnerRepository);
  }
}
