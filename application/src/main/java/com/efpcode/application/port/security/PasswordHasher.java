package com.efpcode.application.port.security;

import com.efpcode.domain.common.model.PlainPassword;
import com.efpcode.domain.user.model.UserPassword;

public interface PasswordHasher {
  UserPassword hash(PlainPassword plainPassword);

  boolean matches(PlainPassword plainPassword, UserPassword hash);
}
