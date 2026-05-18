package com.efpcode.infrastructure.config;

import com.efpcode.application.policy.admin.AdminActionPolicy;
import com.efpcode.application.usecase.user.GetAllStaffUsersUseCase;
import com.efpcode.application.usecase.user.GetAllUsersUseCase;
import com.efpcode.application.usecase.user.GetUserUseCase;
import com.efpcode.domain.user.port.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AdminUserReadConfig {

  @Bean
  public GetAllUsersUseCase getAllUsersUseCase(
      UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    return new GetAllUsersUseCase(userRepository, adminActionPolicy);
  }

  @Bean
  public GetAllStaffUsersUseCase getAllStaffUsersUseCase(
      UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    return new GetAllStaffUsersUseCase(userRepository, adminActionPolicy);
  }

  @Bean
  public GetUserUseCase getUserUseCase(
      UserRepository userRepository, AdminActionPolicy adminActionPolicy) {
    return new GetUserUseCase(userRepository, adminActionPolicy);
  }
}
