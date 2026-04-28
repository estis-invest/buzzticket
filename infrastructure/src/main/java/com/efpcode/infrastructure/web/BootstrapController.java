package com.efpcode.infrastructure.web;

import com.efpcode.application.usecase.bootstrap.RegisterBootstrapUseCase;
import com.efpcode.application.usecase.bootstrap.dto.BootstrapCommand;
import com.efpcode.application.usecase.bootstrap.dto.BootstrapResult;
import com.efpcode.infrastructure.web.dto.BootstrapResponse;
import com.efpcode.infrastructure.web.dto.RegisterBootstrapRequest;
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
@RequestMapping("/api/v1/bootstrap")
class BootstrapController {

  private static final Logger log = LoggerFactory.getLogger(BootstrapController.class);

  private final RegisterBootstrapUseCase bootstrapUseCase;

  BootstrapController(RegisterBootstrapUseCase bootstrapUseCase) {
    this.bootstrapUseCase = bootstrapUseCase;
  }

  @PostMapping
  public ResponseEntity<BootstrapResponse> registerBootstrap(
      @Valid @RequestBody RegisterBootstrapRequest request) {
    log.info("Rest: Bootstrap for new admin and new partner received");

    var command =
        new BootstrapCommand(
            request.name(),
            request.city(),
            request.country(),
            request.isoCode(),
            request.userName(),
            request.userPassword(),
            request.userEmail());

    BootstrapResult result = bootstrapUseCase.execute(command);
    log.info(
        "Rest: Bootstrap for new partner and new admin user {} successfully registered",
        BootstrapResponse.from(result));

    return ResponseEntity.status(HttpStatus.CREATED).body(BootstrapResponse.from(result));
  }
}
