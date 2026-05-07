package com.efpcode.infrastructure.adapters;

import com.efpcode.application.port.in.company.CompanyRegistrationCommands;
import com.efpcode.application.usecase.company.RegisterCompanyUseCase;
import com.efpcode.application.usecase.company.dto.CompanyCommand;
import com.efpcode.application.usecase.company.dto.CompanyResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyRegistrationTransactionalAdapter implements CompanyRegistrationCommands {
  private final RegisterCompanyUseCase useCase;

  public CompanyRegistrationTransactionalAdapter(RegisterCompanyUseCase useCase) {
    this.useCase = useCase;
  }

  @Override
  @Transactional
  public CompanyResult register(CompanyCommand command) {
    return useCase.execute(command);
  }
}
