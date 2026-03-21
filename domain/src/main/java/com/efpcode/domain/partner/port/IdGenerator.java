package com.efpcode.domain.partner.port;

import com.efpcode.domain.partner.model.PartnerId;

public interface IdGenerator {
  PartnerId generate();
}
