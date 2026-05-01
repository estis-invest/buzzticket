package com.efpcode.infrastructure.web;

import com.efpcode.application.usecase.company.RegisterCompanyUseCase;
import com.efpcode.application.usecase.company.dto.CompanyCommand;
import com.efpcode.application.usecase.company.dto.CompanyResult;
import com.efpcode.infrastructure.web.dto.CompanyResponse;
import com.efpcode.infrastructure.web.dto.RegisterCompanyRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/company-registration")
class CompanyRegistrationController {

  private static final Logger log = LoggerFactory.getLogger(CompanyRegistrationController.class);

  private final RegisterCompanyUseCase registerCompanyUseCase;

  CompanyRegistrationController(RegisterCompanyUseCase registerCompanyUseCase) {
    this.registerCompanyUseCase = registerCompanyUseCase;
  }

  @PostMapping
  public ResponseEntity<CompanyResponse> registerCompany(
      @Valid @RequestBody RegisterCompanyRequest request) {
    log.info("Rest: Company for new admin and new partner received");

    var command =
        new CompanyCommand(
            request.name(),
            request.city(),
            request.country(),
            request.isoCode(),
            request.userName(),
            request.userPassword(),
            request.userEmail());

    CompanyResult result = registerCompanyUseCase.execute(command);
    log.info(
        "Rest: Company for new partner and new admin user {} successfully registered",
        CompanyResponse.from(result));

    return ResponseEntity.status(HttpStatus.CREATED).body(CompanyResponse.from(result));
  }
}
