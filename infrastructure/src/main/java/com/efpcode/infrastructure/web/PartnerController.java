package com.efpcode.infrastructure.web;

import com.efpcode.application.usecase.partner.GetAllPartnersUseCase;
import com.efpcode.application.usecase.partner.GetPartnerUseCase;
import com.efpcode.application.usecase.partner.RegisterPartnerUseCase;
import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.infrastructure.web.dto.PartnerListResponse;
import com.efpcode.infrastructure.web.dto.PartnerResponse;
import com.efpcode.infrastructure.web.dto.RegisterPartnerRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/partners")
class PartnerController {

  private final RegisterPartnerUseCase registerPartnerUseCase;
  private final GetPartnerUseCase getPartnerUseCase;
  private final GetAllPartnersUseCase getAllPartnersUseCase;

  PartnerController(
      RegisterPartnerUseCase registerPartnerUseCase,
      GetPartnerUseCase getPartnerUseCase,
      GetAllPartnersUseCase getAllPartnersUseCase) {
    this.registerPartnerUseCase = registerPartnerUseCase;
    this.getPartnerUseCase = getPartnerUseCase;
    this.getAllPartnersUseCase = getAllPartnersUseCase;
  }

  @PostMapping
  public ResponseEntity<PartnerResponse> registerPartner(
      @Valid @RequestBody RegisterPartnerRequest request) {
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

  @GetMapping
  public ResponseEntity<PartnerListResponse> getAllPartners() {
    List<Partner> partners = getAllPartnersUseCase.execute();
    List<PartnerResponse> partnerResponses =
        partners.stream().map(PartnerResponse::fromDomain).toList();
    return ResponseEntity.ok(new PartnerListResponse(partnerResponses, partnerResponses.size()));
  }
}
