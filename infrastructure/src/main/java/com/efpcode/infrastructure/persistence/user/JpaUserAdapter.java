package com.efpcode.infrastructure.persistence.user;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.User;
import com.efpcode.domain.user.model.UserCreatedAt;
import com.efpcode.domain.user.model.UserEmail;
import com.efpcode.domain.user.model.UserId;
import com.efpcode.domain.user.port.UserRepository;
import com.efpcode.infrastructure.persistence.exceptions.MissingReferenceEntityException;
import com.efpcode.infrastructure.persistence.partner.PartnerEntity;
import com.efpcode.infrastructure.persistence.partner.SpringDataPartnerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class JpaUserAdapter implements UserRepository {
  private final SpringDataUserRepository userRepository;
  private final SpringDataPartnerRepository partnerRepository;

  public JpaUserAdapter(
      SpringDataUserRepository userRepository, SpringDataPartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  @Override
  public Optional<User> findUserById(UserId id) {
    return Optional.empty();
  }

  @Override
  public Optional<User> findUserByEmail(UserEmail email) {
    return Optional.empty();
  }

  @Override
  public List<User> findByUserCreatedAtRange(
      PartnerId partnerId, UserCreatedAt startDate, UserCreatedAt endDate) {
    Pageable pageable = PageRequest.of(0, 25);

    return userRepository
        .findByPartner_PartnerIdAndUserCreatedAtBetween(
            partnerId.partnerId(), startDate.time(), endDate.time(), pageable)
        .stream()
        .map(UserMapper::toDomain)
        .toList();
  }

  @Override
  public List<User> findAll() {
    Pageable pageable = PageRequest.of(0, 25);
    return userRepository.findAll(pageable).getContent().stream()
        .map(UserMapper::toDomain)
        .toList();
  }

  @Override
  public List<User> findByPartnerId(PartnerId id) {
    return List.of();
  }

  @Override
  public boolean existsByEmail(UserEmail email) {
    return false;
  }

  @Override
  public void save(User user) {
    PartnerEntity partnerEntity =
        user.partnerId()
            .map(PartnerId::partnerId)
            .map(
                id ->
                    partnerRepository
                        .findByPartnerId(id)
                        .orElseThrow(() -> new MissingReferenceEntityException("Partner", id)))
            .orElse(null);

    UserEntity entity = UserMapper.toEntity(user, partnerEntity);
    userRepository.save(entity);
  }

  @Override
  public void deleteByUserId(UserId id) {
    userRepository.deleteById(id.id());
  }
}
