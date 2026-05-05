package com.efpcode.application.port.security;

import com.efpcode.domain.user.model.UserPassword;

public interface DummyPasswordHashProvider {
  UserPassword dummyHash();
}
