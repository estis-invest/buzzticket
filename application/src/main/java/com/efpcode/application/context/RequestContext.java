package com.efpcode.application.context;

import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.model.UserRole;

public interface RequestContext {

  UserId userId();

  UserRole role();
}
