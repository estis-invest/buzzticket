package com.efpcode.infrastructure.persistence.user;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

  List<UserEntity> findByPartner_PartnerIdAndUserCreatedAtBetween(
      UUID partnerPartnerId, Instant userCreatedAtAfter, Instant userCreatedAtBefore, Sort sort);

  List<UserEntity> findByPartner_PartnerId(UUID partnerPartnerId, Sort sort);

  Optional<UserEntity> findByUserId(UUID userId);

  Optional<UserEntity> findByUserEmailIgnoreCase(String userEmail);

  boolean existsByUserEmailIgnoreCase(String userEmail);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query(
"""
SELECT u FROM UserEntity u WHERE u.partner.partnerId = :partnerId AND u.userRole = 'ADMIN'
""")
  List<UserEntity> findAdminsForUpdate(@Param("partnerId") UUID partnerId);

  @Query(
"""
    SELECT u FROM UserEntity u
    LEFT JOIN u.partner p
    WHERE
        u.userRole = 'CUSTOMER'
        OR (
            u.userRole IN ('ADMIN', 'SUPPORT')
            AND p.partnerId = :partnerId
        )
""")
  List<UserEntity> findVisibleUsersForAdmin(@Param("partnerId") UUID partnerId);
}
