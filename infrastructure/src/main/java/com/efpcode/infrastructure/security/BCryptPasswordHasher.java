package com.efpcode.infrastructure.security;

import com.efpcode.application.port.security.PasswordHasher;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.UserPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

  @Override
  public UserPassword hash(PlainPassword plainPassword) {
    String encodedPassword = encoder.encode(plainPassword.plainPassword());
    return UserPassword.fromHash(encodedPassword);
  }

  @Override
  public boolean matches(PlainPassword plainPassword, UserPassword hash) {
    return encoder.matches(plainPassword.plainPassword(), hash.hashedPassword());
  }
}
