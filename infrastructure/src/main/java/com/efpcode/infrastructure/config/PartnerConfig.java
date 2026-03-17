package com.efpcode.infrastructure.config;

import com.efpcode.application.usecase.partner.GetAllPartnersUseCase;
import com.efpcode.application.usecase.partner.GetPartnerUseCase;
import com.efpcode.application.usecase.partner.RegisterPartnerUseCase;
import com.efpcode.domain.partner.port.IdGenerator;
import com.efpcode.domain.partner.port.PartnerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PartnerConfig {

  @Bean
  public RegisterPartnerUseCase registerPartnerUseCase(
      PartnerRepository partnerRepository, IdGenerator idGenerator) {
    return new RegisterPartnerUseCase(partnerRepository, idGenerator);
  }

  @Bean
  public GetPartnerUseCase getPartnerUseCase(PartnerRepository partnerRepository) {
    return new GetPartnerUseCase(partnerRepository);
  }

  @Bean
  public GetAllPartnersUseCase getAllPartnersUseCase(PartnerRepository partnerRepository) {
    return new GetAllPartnersUseCase(partnerRepository);
  }
}
