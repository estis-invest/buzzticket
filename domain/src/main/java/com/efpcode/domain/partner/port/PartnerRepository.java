package com.efpcode.domain.partner.port;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerName;
import java.util.List;
import java.util.Optional;

public interface PartnerRepository {

  void save(Partner partner);

  /**
   * Soft Deletes a partner from the repository by their unique identifier. Changes the status of
   * the partner to DELETED.
   *
   * @param id the unique identifier of the partner to be deleted; must not be null
   * @throws InvalidPartnerIdException if the provided PartnerId is null
   * @throws IllegalPartnerIdArgumentException if the provided PartnerId is invalid
   */
  void delete(PartnerId id);

  boolean existsById(PartnerId id);

  boolean existsByName(PartnerName name);

  Optional<Partner> findById(PartnerId id);

  List<Partner> findAll();
}
