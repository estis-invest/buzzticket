package com.efpcode.application.usecase.shared;

import com.efpcode.application.usecase.user.exceptions.IllegalUserNotFoundException;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;
import java.util.UUID;

public final class UserResolver {
  private UserResolver() {}
  ;

  public static User resolveRequired(UserRepository userRepository, UUID uuid) {

    if (uuid == null) {
      throw new IllegalUserNotFoundException("Required value is missing");
    }

    UserId targetUserId = UserId.of(uuid);

    User targetUser =
        userRepository
            .findUserById(targetUserId)
            .orElseThrow(() -> new IllegalUserNotFoundException("User not found"));

    return targetUser;
  }
}
