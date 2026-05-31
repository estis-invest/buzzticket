package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.StaffInvitationTimeToLivePolicy;
import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.policy.partner.PartnerAdminInvariantPolicy;
import com.efpcode.application.policy.user.UserAuthenticationPolicy;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.application.port.out.security.StaffInvitationTokenGenerator;
import com.efpcode.application.port.out.security.StaffInvitationTokenHasher;
import com.efpcode.application.usecase.user.*;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.staffinvitation.model.StaffInvitationId;
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

  @Bean
  public AcceptStaffInvitationUseCase acceptStaffInvitationUseCase(
      IdGenerator<UserId> idGenerator,
      UserRepository userRepository,
      StaffInvitationRepository staffInvitationRepository,
      StaffInvitationTokenHasher tokenHasher,
      Clock clock,
      PasswordHasher passwordHasher) {
    return new AcceptStaffInvitationUseCase(
        idGenerator, userRepository, staffInvitationRepository, tokenHasher, clock, passwordHasher);
  }

  @Bean
  public RegisterCustomerUseCase registerCustomerUseCase(
      IdGenerator<UserId> idGenerator, UserRepository repository, PasswordHasher passwordHasher) {

    return new RegisterCustomerUseCase(idGenerator, repository, passwordHasher);
  }

  @Bean
  public UserNameUpdateUseCase userNameUpdateUseCase(
      UserRepository userRepository, UserAuthenticationPolicy authenticationPolicy) {
    return new UserNameUpdateUseCase(userRepository, authenticationPolicy);
  }

  @Bean
  public UserEmailUpdateUseCase userEmailUpdateUseCase(
      UserRepository userRepository, UserAuthenticationPolicy userAuthenticationPolicy) {
    return new UserEmailUpdateUseCase(userRepository, userAuthenticationPolicy);
  }

  @Bean
  public UserPasswordUpdateUseCase userPasswordUpdateUseCase(
      UserRepository userRepository,
      UserAuthenticationPolicy authenticationPolicy,
      PasswordHasher passwordHasher) {
    return new UserPasswordUpdateUseCase(userRepository, authenticationPolicy, passwordHasher);
  }

  @Bean
  public AccountDeactivationUseCase accountDeactivationUseCase(
      UserRepository userRepository,
      UserAuthenticationPolicy authenticationPolicy,
      PartnerAdminInvariantPolicy partnerAdminInvariantPolicy,
      PasswordHasher passwordHasher) {

    return new AccountDeactivationUseCase(
        userRepository, authenticationPolicy, partnerAdminInvariantPolicy, passwordHasher);
  }

  @Bean
  public AdminAccountDeactivationUseCase adminAccountDeactivationUseCase(
      UserRepository userRepository,
      AdminActionPolicy adminActionPolicy,
      PartnerAdminInvariantPolicy partnerAdminInvariantPolicy) {
    return new AdminAccountDeactivationUseCase(
        userRepository, adminActionPolicy, partnerAdminInvariantPolicy);
  }

  @Bean
  public AdminAccountActivateUseCase adminAccountActivateUseCase(
      UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    return new AdminAccountActivateUseCase(userRepository, adminActionPolicy);
  }

  @Bean
  public AdminAccountPromotionUseCase adminAccountPromotionUseCase(
      UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    return new AdminAccountPromotionUseCase(userRepository, adminActionPolicy);
  }

  @Bean
  public AdminAccountDemotionUseCase adminAccountDemotionUseCase(
      UserRepository userRepository,
      AdminActionPolicy adminActionPolicy,
      PartnerAdminInvariantPolicy partnerAdminInvariantPolicy) {
    return new AdminAccountDemotionUseCase(
        userRepository, adminActionPolicy, partnerAdminInvariantPolicy);
  }
}
