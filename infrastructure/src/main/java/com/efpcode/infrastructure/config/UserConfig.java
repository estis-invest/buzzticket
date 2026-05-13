package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.StaffInvitationTimeToLivePolicy;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.port.out.security.StaffInvitationTokenGenerator;
import com.efpcode.application.port.out.security.StaffInvitationTokenHasher;
import com.efpcode.application.usecase.user.CreateStaffInvitationUseCase;
import com.efpcode.application.usecase.user.RegisterStaffUseCase;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.staffinvitation.port.StaffInvitationRepository;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

  @Bean
  public RegisterStaffUseCase registerStaffUseCase(
      IdGenerator<UserId> idGenerator,
      UserRepository userRepository,
      AdminActionPolicy adminActionPolicy,
      PasswordHasher passwordHasher) {
    return new RegisterStaffUseCase(idGenerator, userRepository, adminActionPolicy, passwordHasher);
  }

  @Bean
  public CreateStaffInvitationUseCase createStaffInvitationUseCase(
      IdGenerator<StaffInvitationId> idGenerator,
      StaffInvitationRepository staffInvitationRepository,
      AdminActionPolicy adminActionPolicy,
      StaffInvitationTokenHasher tokenHasher,
      Clock clock,
      StaffInvitationTokenGenerator tokenGenerator,
      StaffInvitationTimeToLivePolicy timeToLivePolicy) {
    return new CreateStaffInvitationUseCase(
        idGenerator,
        staffInvitationRepository,
        adminActionPolicy,
        tokenHasher,
        clock,
        tokenGenerator,
        timeToLivePolicy);
  }
}
