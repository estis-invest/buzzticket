package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.StaffInvitationQueryReads;
import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.dto.CreateStaffInvitationResult;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationCommand;
import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.domain.staffinvitation.StaffInvitationStatus;
import com.efpcode.infrastructure.config.properties.FrontendProperties;
import com.efpcode.infrastructure.web.dto.requests.RegisterStaffInvitationRequest;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationCreateResponse;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationQueryListResponse;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationQueryResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/staff")
class StaffInvitationCommandController {

  private final StaffRegistrationCommands staffRegistrationCommands;
  private final StaffInvitationQueryReads staffInvitationQueryReads;
  private final FrontendProperties frontendProperties;

  public StaffInvitationCommandController(
      StaffRegistrationCommands staffRegistrationCommands,
      StaffInvitationQueryReads staffInvitationQueryReads,
      FrontendProperties frontendProperties) {
    this.staffRegistrationCommands = staffRegistrationCommands;
    this.staffInvitationQueryReads = staffInvitationQueryReads;
    this.frontendProperties = frontendProperties;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/invitations")
  public ResponseEntity<StaffInvitationCreateResponse> sendInvite(
      @Valid @RequestBody RegisterStaffInvitationRequest request) {
    var command =
        new RegisterStaffInvitationCommand(
            request.inviteeEmail(), request.role().toUpperCase(Locale.ROOT), request.expiresAt());

    CreateStaffInvitationResult invitationResult =
        staffRegistrationCommands.sendInvitation(command);
    String inviteLink = generateLink(invitationResult.rawToken());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new StaffInvitationCreateResponse(
                invitationResult.invitationId(),
                invitationResult.inviteeEmail(),
                invitationResult.role(),
                invitationResult.status(),
                invitationResult.expiresAt(),
                inviteLink));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/invitations/{id}/cancel")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelStaffInvitation(@PathVariable UUID id) {
    staffRegistrationCommands.cancelInvitation(new StaffInvitationId(id));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/invitations/{id}")
  public ResponseEntity<StaffInvitationQueryResponse> getStaffInvitation(@PathVariable UUID id) {
    StaffInvitationQueryResult result =
        staffInvitationQueryReads.getStaffInvitation(new StaffInvitationId(id));
    return ResponseEntity.ok(StaffInvitationQueryResponse.fromResult(result));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/invitations")
  public ResponseEntity<StaffInvitationQueryListResponse> getAllStaffInvitations(
      @RequestParam StaffInvitationStatus status) {

    List<StaffInvitationQueryResult> invitationQueryResults =
        staffInvitationQueryReads.getAllStaffInvitationByStatus(status);

    List<StaffInvitationQueryResponse> invitationQueryResponses =
        invitationQueryResults.stream().map(StaffInvitationQueryResponse::fromResult).toList();

    return ResponseEntity.ok(
        new StaffInvitationQueryListResponse(
            invitationQueryResponses, invitationQueryResponses.size()));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/invitations/exists")
  public ResponseEntity<Boolean> hasInvitationByEmail(@RequestParam String inviteeEmail) {

    boolean hasInvite = staffInvitationQueryReads.hasPendingInvite(inviteeEmail);

    return ResponseEntity.ok(hasInvite);
  }

  private String generateLink(String token) {
    var tokenStem = "/public/staff/invitations/accept";
    return UriComponentsBuilder.fromUriString(frontendProperties.baseUrl())
        .path(tokenStem)
        .queryParam("token", token)
        .build()
        .toUriString();
  }
}
