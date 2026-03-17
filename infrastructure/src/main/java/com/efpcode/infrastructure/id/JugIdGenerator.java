package com.efpcode.infrastructure.id;

import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.IdGenerator;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import org.springframework.stereotype.Component;

@Component
public class JugIdGenerator implements IdGenerator {
  private final NoArgGenerator v7Generator = Generators.timeBasedEpochGenerator();

  @Override
  public PartnerId generate() {
    return new PartnerId(v7Generator.generate());
  }
}
