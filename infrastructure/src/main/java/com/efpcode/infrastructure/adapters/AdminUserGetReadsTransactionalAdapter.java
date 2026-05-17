package com.efpcode.infrastructure.adapters;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.user.AdminUserReads;
import com.efpcode.application.usecase.user.GetAllStaffUsersUseCase;
import com.efpcode.application.usecase.user.GetAllUsersUseCase;
import com.efpcode.application.usecase.user.GetUserUseCase;
import com.efpcode.application.usecase.user.dto.UserResult;
import com.efpcode.domain.user.model.UserId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserGetReadsTransactionalAdapter implements AdminUserReads {

  private final RequestContext requestContext;
  private final GetAllUsersUseCase getAllUsersUseCase;
  private final GetAllStaffUsersUseCase getAllStaffUsersUseCase;
  private final GetUserUseCase getUserUseCase;

  public AdminUserGetReadsTransactionalAdapter(
      RequestContext requestContext,
      GetAllUsersUseCase getAllUsersUseCase,
      GetAllStaffUsersUseCase getAllStaffUsersUseCase,
      GetUserUseCase getUserUseCase) {
    this.requestContext = requestContext;
    this.getAllUsersUseCase = getAllUsersUseCase;
    this.getAllStaffUsersUseCase = getAllStaffUsersUseCase;
    this.getUserUseCase = getUserUseCase;
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResult> getUsers() {
    return getAllUsersUseCase.execute(requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResult> staffUsers() {
    return getAllStaffUsersUseCase.execute(requestContext);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResult getUser(UserId id) {
    return getUserUseCase.execute(requestContext, id);
  }
}
