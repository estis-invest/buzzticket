package com.efpcode.application.usecase.auth;

import com.efpcode.application.port.security.DummyPasswordHashProvider;
import com.efpcode.application.port.security.JwtTokenIssuer;
import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.application.usecase.auth.dto.AuthResult;
import com.efpcode.application.usecase.auth.dto.RegisterLoginCommand;
import com.efpcode.application.usecase.auth.exceptions.LoginFailException;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.model.UserPassword;
import com.efpcode.domain.user.port.UserRepository;

public class GetLoginUseCase {

  private final UserRepository userRepository;
  private final PasswordHasher passwordHasher;
  private final JwtTokenIssuer jwtTokenIssuer;
  private final DummyPasswordHashProvider dummyPasswordHashProvider;
  private static final String ERROR_MESSAGE = "Invalid email or password";

  public GetLoginUseCase(
      UserRepository userRepository,
      PasswordHasher passwordHasher,
      JwtTokenIssuer jwtTokenIssuer,
      DummyPasswordHashProvider dummyPasswordHashProvider) {
    this.userRepository = userRepository;
    this.passwordHasher = passwordHasher;
    this.jwtTokenIssuer = jwtTokenIssuer;
    this.dummyPasswordHashProvider = dummyPasswordHashProvider;
  }

  public AuthResult execute(RegisterLoginCommand command) {

    UserEmail email = new UserEmail(command.email());
    PlainPassword plainPassword = new PlainPassword(command.password());

    User user = userRepository.findUserByEmail(email).orElse(null);

    if (!safeMatches(
            plainPassword, user == null ? dummyPasswordHashProvider.dummyHash() : user.password())
        || user == null) {
      throw new LoginFailException(ERROR_MESSAGE);
    }

    return new AuthResult(jwtTokenIssuer.issueToken(user));
  }

  private boolean safeMatches(PlainPassword plainPassword, UserPassword hash) {
    try {
      return passwordHasher.matches(plainPassword, hash);
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}
