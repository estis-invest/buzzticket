package com.efpcode.infrastructure.security;

import com.efpcode.application.port.out.security.DummyPasswordHashProvider;
import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.UserPassword;
import org.springframework.stereotype.Component;

@Component
public class BCryptDummyPasswordHashProvider implements DummyPasswordHashProvider {

  private final UserPassword dummyHash;

  public BCryptDummyPasswordHashProvider(PasswordHasher hasher) {
    this.dummyHash = hasher.hash(new PlainPassword("__DUMMY_PASSWORD_123!_DO_NOT_USE__"));
  }

  @Override
  public UserPassword dummyHash() {
    return dummyHash;
  }
}
