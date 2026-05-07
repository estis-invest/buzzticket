package com.efpcode.infrastructure.testhelper;

import com.efpcode.application.port.out.security.PasswordHasher;
import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.UserPassword;

public class TestPasswordHasher implements PasswordHasher {

  @Override
  public UserPassword hash(PlainPassword plainPassword) {
    return new UserPassword(plainPassword.plainPassword());
  }

  @Override
  public boolean matches(PlainPassword plainPassword, UserPassword hash) {
    return plainPassword.plainPassword().equals(hash.hashedPassword());
  }
}
