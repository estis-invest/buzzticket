package com.efpcode.application.port.in.company;

import com.efpcode.application.usecase.company.dto.CompanyCommand;
import com.efpcode.application.usecase.company.dto.CompanyResult;

public interface CompanyRegistrationCommands {
    CompanyResult register (CompanyCommand command);
}
