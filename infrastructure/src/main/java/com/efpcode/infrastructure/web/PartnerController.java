package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.partner.PartnerLifeCycleCommands;
import com.efpcode.application.usecase.partner.*;
import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.infrastructure.web.dto.PartnerListResponse;
import com.efpcode.infrastructure.web.dto.PartnerResponse;
import com.efpcode.infrastructure.web.dto.RegisterPartnerRequest;
import com.efpcode.infrastructure.web.dto.UpdatePartnerRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/partners")
class PartnerController {
  private static final Logger log = LoggerFactory.getLogger(PartnerController.class);

  private final GetPartnerUseCase getPartnerUseCase;
  private final GetAllPartnersUseCase getAllPartnersUseCase;
  private final PartnerLifeCycleCommands partnerLifeCycleCommands;

  PartnerController(
      GetPartnerUseCase getPartnerUseCase,
      GetAllPartnersUseCase getAllPartnersUseCase,
      PartnerLifeCycleCommands partnerLifeCycleCommands) {
    this.getPartnerUseCase = getPartnerUseCase;
    this.getAllPartnersUseCase = getAllPartnersUseCase;
    this.partnerLifeCycleCommands = partnerLifeCycleCommands;
  }

  @PostMapping
  public ResponseEntity<PartnerResponse> registerPartner(
      @Valid @RequestBody RegisterPartnerRequest request) {
    log.info("REST: Registering request for new partner: {}", request);
    var command =
        new RegisterPartnerCommand(
            request.name(), request.city(), request.country(), request.isoCode());

    Partner partner = partnerLifeCycleCommands.register(command);
    log.info("REST: Registered new partner: {} successfully", partner);
    return ResponseEntity.status(HttpStatus.CREATED).body(PartnerResponse.fromDomain(partner));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PartnerResponse> getPartner(@PathVariable UUID id) {
    Partner partner = getPartnerUseCase.execute(new PartnerId(id));
    return ResponseEntity.ok(PartnerResponse.fromDomain(partner));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<PartnerListResponse> getAllPartners() {
    List<Partner> partners = getAllPartnersUseCase.execute();
    List<PartnerResponse> partnerResponses =
        partners.stream().map(PartnerResponse::fromDomain).toList();
    return ResponseEntity.ok(new PartnerListResponse(partnerResponses, partnerResponses.size()));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePartner(@PathVariable UUID id) {
    log.info("REST: Deleting request for partner with id: {}", id);
    partnerLifeCycleCommands.delete(new PartnerId(id));
    log.info("REST: Deleted partner with id: {} successfully", id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<PartnerResponse> deactivatePartner(@PathVariable UUID id) {
    log.info("REST: Deactivation request for partner: {}", id);

    Partner partner = partnerLifeCycleCommands.deactivate(new PartnerId(id));

    log.info("REST: Partner {} deactivated", id);
    return ResponseEntity.ok(PartnerResponse.fromDomain(partner));
  }

  @PatchMapping("/{id}/activate")
  public ResponseEntity<PartnerResponse> activatePartner(@PathVariable UUID id) {
    log.info("REST: Activation request for partner: {}", id);
    Partner partner = partnerLifeCycleCommands.activate(new PartnerId(id));
    log.info("REST: Partner {} activated", id);
    return ResponseEntity.ok(PartnerResponse.fromDomain(partner));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<PartnerResponse> editPartner(
      @RequestBody UpdatePartnerRequest request, @PathVariable UUID id) {
    log.info("REST: Editing request for partner: {}", id);
    var command =
        new UpdatePartnerCommand(
            request.getNormalizedName(),
            request.getNormalizedCity(),
            request.getNormalizedCountry(),
            request.getNormalizedIsoCode());
    PartnerId partnerId = new PartnerId(id);
    Partner editPartner = partnerLifeCycleCommands.edit(partnerId, command);
    log.info(
        "REST: Partner with id {} successfully edit to: {}",
        editPartner.id().partnerId(),
        editPartner);
    return ResponseEntity.ok(PartnerResponse.fromDomain(editPartner));
  }
}
