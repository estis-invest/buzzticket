package com.efpcode.domain.partner.port;

import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerName;
import java.util.List;
import java.util.Optional;

public interface PartnerRepository {

  void save(Partner partner);

  boolean existsById(PartnerId id);

  boolean existsByName(PartnerName name);

  Optional<Partner> findById(PartnerId id);

  List<Partner> findAll();
}
