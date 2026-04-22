package com.efpcode.domain.user.port;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserCreatedAt;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.model.UserId;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findUserById(UserId id);

  Optional<User> findUserByEmail(UserEmail email);

  List<User> findByUserCreatedAtRange(
      PartnerId partnerId, UserCreatedAt startDate, UserCreatedAt endDate);

  List<User> findAll();

  List<User> findByPartnerId(PartnerId id);

  boolean existsByEmail(UserEmail email);

  void save(User user);

  void deleteByUserId(UserId id);
}
