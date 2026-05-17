package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.AdminUserReads;
import com.efpcode.application.usecase.user.GetAllUsersUseCase;
import com.efpcode.application.usecase.user.dto.UserResult;
import com.efpcode.domain.user.model.UserId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserGetReadsTransactionalAdapter implements AdminUserReads {

  private final RequestContext requestContext;
  private final GetAllUsersUseCase getAllUsersUseCase;

  public AdminUserGetReadsTransactionalAdapter(
      RequestContext requestContext, GetAllUsersUseCase getAllUsersUseCase) {
    this.requestContext = requestContext;
    this.getAllUsersUseCase = getAllUsersUseCase;
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResult> getUsers() {
    return getAllUsersUseCase.execute(requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResult> staffUsers() {
    return List.of();
  }

  @Override
  @Transactional(readOnly = true)
  public UserResult getUser(UserId id) {
    return null;
  }
}
