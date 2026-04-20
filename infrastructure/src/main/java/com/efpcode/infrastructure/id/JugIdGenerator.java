package com.efpcode.infrastructure.id;

import com.efpcode.domain.common.port.IdGenerator;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import java.util.UUID;
import java.util.function.Function;

public class JugIdGenerator<T> implements IdGenerator<T> {

  private final NoArgGenerator v7Generator = Generators.timeBasedEpochGenerator();
  private final Function<UUID, T> mapper;

  public JugIdGenerator(Function<UUID, T> mapper) {
    this.mapper = mapper;
  }

  @Override
  public T generate() {
    return mapper.apply(v7Generator.generate());
  }
}
