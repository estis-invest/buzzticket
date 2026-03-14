package com.efpcode.infrastructure.web;

import com.efpcode.application.usecase.partner.GetPartnerUseCase;
import com.efpcode.application.usecase.partner.RegisterPartnerUseCase;
import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.infrastructure.web.dto.PartnerResponse;
import com.efpcode.infrastructure.web.dto.RegisterPartnerRequest;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/partners")
class PartnerController {

  private final RegisterPartnerUseCase registerPartnerUseCase;
  private final GetPartnerUseCase getPartnerUseCase;

  public PartnerController(
      RegisterPartnerUseCase registerPartnerUseCase, GetPartnerUseCase getPartnerUseCase) {
    this.registerPartnerUseCase = registerPartnerUseCase;
    this.getPartnerUseCase = getPartnerUseCase;
  }

  @PostMapping
  public ResponseEntity<PartnerResponse> registerPartner(
      @RequestBody RegisterPartnerRequest request) {
    var command =
        new RegisterPartnerCommand(
            request.name(), request.city(), request.country(), request.isoCode());

    Partner partner = registerPartnerUseCase.execute(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(PartnerResponse.fromDomain(partner));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PartnerResponse> getPartner(@PathVariable UUID id) {
    Partner partner = getPartnerUseCase.execute(new PartnerId(id));
    return ResponseEntity.ok(PartnerResponse.fromDomain(partner));
  }
}
