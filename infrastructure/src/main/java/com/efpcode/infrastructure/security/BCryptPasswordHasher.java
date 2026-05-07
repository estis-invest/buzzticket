package com.efpcode.infrastructure.security;

import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.UserPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
  private static final int BCRYPT_STRENGTH = 12;

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(BCRYPT_STRENGTH);

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
