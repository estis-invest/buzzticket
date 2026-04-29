package com.efpcode.infrastructure.persistence.partner;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerName;
import com.efpcode.domain.partner.model.PartnerStatus;
import com.efpcode.domain.partner.port.PartnerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class JpaPartnerAdapter implements PartnerRepository {
  private final SpringDataPartnerRepository partnerRepository;

  public JpaPartnerAdapter(SpringDataPartnerRepository partnerRepository) {
    this.partnerRepository = partnerRepository;
  }

  @Override
  public void save(Partner partner) {
    PartnerEntity entity = PartnerMapper.toEntity(partner);
    partnerRepository.save(entity);
  }

  @Override
  public boolean existsById(PartnerId id) {
    return partnerRepository.existsByPartnerIdAndPartnerStatusNot(
        id.partnerId(), PartnerStatus.DELETED.name());
  }

  @Override
  public boolean existsByName(PartnerName name) {
    return partnerRepository.existsByIgnoringCasePartnerNameAndPartnerStatusNot(
        name.partnerName(), PartnerStatus.DELETED.name());
  }

  @Override
  public boolean existsAny() {
    return partnerRepository.existsBy();
  }

  @Override
  public Optional<Partner> findById(PartnerId id) {
    return partnerRepository
        .findByPartnerIdAndPartnerStatusNot(id.partnerId(), PartnerStatus.DELETED.name())
        .map(PartnerMapper::toDomain);
  }

  @Override
  public List<Partner> findAll() {
    return partnerRepository.findAllByPartnerStatusNot(PartnerStatus.DELETED.name()).stream()
        .map(PartnerMapper::toDomain)
        .toList();
  }
}
