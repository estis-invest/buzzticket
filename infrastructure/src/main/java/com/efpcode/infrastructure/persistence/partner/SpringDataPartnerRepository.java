package com.efpcode.infrastructure.persistence.partner;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPartnerRepository extends JpaRepository<PartnerEntity, UUID> {
  boolean existsByPartnerName(String partnerName);
}
