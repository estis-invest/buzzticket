package com.efpcode.infrastructure.config;

import com.efpcode.application.usecase.partner.*;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PartnerConfig {

  @Bean
  public RegisterPartnerUseCase registerPartnerUseCase(
      PartnerRepository partnerRepository, IdGenerator<PartnerId> idGenerator) {
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

  @Bean
  public DeletePartnerUseCase deletePartnerUseCase(PartnerRepository partnerRepository) {
    return new DeletePartnerUseCase(partnerRepository);
  }

  @Bean
  public DeactivatePartnerUseCase deactivatePartnerUseCase(PartnerRepository partnerRepository) {
    return new DeactivatePartnerUseCase(partnerRepository);
  }

  @Bean
  public ActivatePartnerUseCase activatePartnerUseCase(PartnerRepository partnerRepository) {
    return new ActivatePartnerUseCase(partnerRepository);
  }

  @Bean
  public EditPartnerUseCase editPartnerUseCase(PartnerRepository partnerRepository) {
    return new EditPartnerUseCase(partnerRepository);
  }
}
