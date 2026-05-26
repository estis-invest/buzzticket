package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.partner.PartnerAdminActionPolicy;
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
      PartnerRepository partnerRepository,
      IdGenerator<PartnerId> idGenerator,
      PartnerAdminActionPolicy partnerAdminActionPolicy) {
    return new RegisterPartnerUseCase(partnerRepository, idGenerator, partnerAdminActionPolicy);
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
  public DeletePartnerUseCase deletePartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    return new DeletePartnerUseCase(partnerRepository, partnerAdminActionPolicy);
  }

  @Bean
  public DeactivatePartnerUseCase deactivatePartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    return new DeactivatePartnerUseCase(partnerRepository, partnerAdminActionPolicy);
  }

  @Bean
  public ActivatePartnerUseCase activatePartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    return new ActivatePartnerUseCase(partnerRepository, partnerAdminActionPolicy);
  }

  @Bean
  public EditPartnerUseCase editPartnerUseCase(
      PartnerRepository partnerRepository, PartnerAdminActionPolicy partnerAdminActionPolicy) {
    return new EditPartnerUseCase(partnerRepository, partnerAdminActionPolicy);
  }
}
