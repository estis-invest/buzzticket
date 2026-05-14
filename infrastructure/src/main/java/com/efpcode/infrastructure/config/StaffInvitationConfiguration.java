package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.usecase.user.GetStaffInvitationUseCase;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StaffInvitationConfiguration {

  @Bean
  public GetStaffInvitationUseCase getStaffInvitationUseCase(
      StaffInvitationRepository staffInvitationRepository, AdminActionPolicy adminActionPolicy) {
    return new GetStaffInvitationUseCase(staffInvitationRepository, adminActionPolicy);
  }
}
