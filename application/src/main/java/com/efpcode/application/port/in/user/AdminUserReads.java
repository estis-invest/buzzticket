package com.efpcode.application.port.in.user;

import com.efpcode.application.usecase.user.dto.UserResult;
import com.efpcode.domain.user.model.UserId;
import java.util.List;

public interface AdminUserReads {
  List<UserResult> getUsers();

  List<UserResult> staffUsers();

  UserResult getUser(UserId id);
}
