package com.efpcode.application.usecase.user;

import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.domain.common.port.IdGenerator;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;

public class RegisterAdminUserUseCase {
  private final UserRepository userRepository;
  private final IdGenerator<UserId> userIdIdGenerator;
  private final PasswordHasher passwordHasher;

  public RegisterAdminUserUseCase(
      UserRepository userRepository,
      IdGenerator<UserId> userIdIdGenerator,
      PasswordHasher passwordHasher) {
    this.userRepository = userRepository;
    this.userIdIdGenerator = userIdIdGenerator;
    this.passwordHasher = passwordHasher;
  }
}
