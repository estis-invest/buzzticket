package com.efpcode.application.port.in.auth;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.usecase.auth.dto.AuthResult;
import com.efpcode.application.usecase.auth.dto.AuthenticatedUserResult;

public interface AuthenticatedSession {

  AuthResult refresh(RequestContext requestContext);

  AuthenticatedUserResult me(RequestContext requestContext);
}
