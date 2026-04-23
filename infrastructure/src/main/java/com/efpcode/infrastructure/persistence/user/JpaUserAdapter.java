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
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class JpaUserAdapter implements UserRepository {
  private final SpringDataUserRepository userRepository;
  private final SpringDataPartnerRepository partnerRepository;

  private static final int DEFAULT_PAGE_SIZE = 25;

  public JpaUserAdapter(
      SpringDataUserRepository userRepository, SpringDataPartnerRepository partnerRepository) {
    this.userRepository = userRepository;
    this.partnerRepository = partnerRepository;
  }

  @Override
  public Optional<User> findUserById(UserId id) {
    return userRepository.findByUserId(id.id()).map(UserMapper::toDomain);
  }

  @Override
  public Optional<User> findUserByEmail(UserEmail email) {
    return userRepository.findByUserEmail(email.email()).map(UserMapper::toDomain);
  }

  @Override
  public List<User> findByUserCreatedAtRange(
      PartnerId partnerId, UserCreatedAt startDate, UserCreatedAt endDate) {
    Pageable pageable = getPageable();

    return userRepository
        .findByPartner_PartnerIdAndUserCreatedAtBetween(
            partnerId.partnerId(), startDate.time(), endDate.time(), pageable)
        .stream()
        .map(UserMapper::toDomain)
        .toList();
  }

  @Override
  public List<User> findAll() {
    Pageable pageable = getPageable();
    return userRepository.findAll(pageable).getContent().stream()
        .map(UserMapper::toDomain)
        .toList();
  }

  @Override
  public List<User> findByPartnerId(PartnerId id) {
    Pageable pageable = getPageable();
    return userRepository.findByPartner_PartnerId(id.partnerId(), pageable).stream()
        .map(UserMapper::toDomain)
        .toList();
  }

  @Override
  public boolean existsByEmail(UserEmail email) {
    return userRepository.existsByUserEmailIgnoreCase(email.email());
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

  // Helper methods
  private static @NonNull Pageable getPageable() {
    Sort sort = Sort.by("userCreatedAt").descending();
    return PageRequest.of(0, DEFAULT_PAGE_SIZE, sort);
  }
}
