package com.efpcode.infrastructure.persistence.partner;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerName;
import com.efpcode.domain.partner.port.PartnerRepository;
import com.efpcode.infrastructure.persistence.partner.exceptions.PartnerNotFoundByIdException;
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
  public void delete(PartnerId id) {
    Partner partnerDelete =
        findById(id)
            .orElseThrow(
                () ->
                    new PartnerNotFoundByIdException(
                        "Partner not found with id: " + id.partnerId()));
    Partner deleted = partnerDelete.toDelete();

    partnerRepository.save(PartnerMapper.toEntity(deleted));
  }

  @Override
  public boolean existsById(PartnerId id) {
    return partnerRepository.existsById(id.partnerId());
  }

  @Override
  public boolean existsByName(PartnerName name) {
    return partnerRepository.existsByPartnerName(name.partnerName());
  }

  @Override
  public Optional<Partner> findById(PartnerId id) {
    return partnerRepository.findById(id.partnerId()).map(PartnerMapper::toDomain);
  }

  @Override
  public List<Partner> findAll() {
    return partnerRepository.findAll().stream().map(PartnerMapper::toDomain).toList();
  }
}
