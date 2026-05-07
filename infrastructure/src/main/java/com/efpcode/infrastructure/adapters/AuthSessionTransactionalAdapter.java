package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.auth.AuthenticatedSession;
import com.efpcode.application.usecase.auth.GetAuthenticatedUserUseCase;
import com.efpcode.application.usecase.auth.RefreshSessionUseCase;
import com.efpcode.application.usecase.auth.dto.AuthResult;
import com.efpcode.application.usecase.auth.dto.AuthenticatedUserResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class AuthSessionTransactionalAdapter implements AuthenticatedSession {

  private final RefreshSessionUseCase refreshSessionUseCase;
  private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

  public AuthSessionTransactionalAdapter(
      RefreshSessionUseCase refreshSessionUseCase,
      GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
    this.refreshSessionUseCase = refreshSessionUseCase;
    this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
  }

  @Override
  public AuthResult refresh(RequestContext requestContext) {
    return refreshSessionUseCase.execute(requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public AuthenticatedUserResult me(RequestContext requestContext) {
    return getAuthenticatedUserUseCase.execute(requestContext);
  }
}
