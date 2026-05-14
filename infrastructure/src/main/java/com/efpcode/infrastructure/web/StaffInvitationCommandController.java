package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.StaffInvitationQueryCommands;
import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.dto.CreateStaffInvitationResult;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationCommand;
import com.efpcode.application.usecase.user.dto.StaffInvitationQueryResult;
import com.efpcode.domain.staffinvitation.StaffInvitationId;
import com.efpcode.infrastructure.config.properties.FrontendProperties;
import com.efpcode.infrastructure.web.dto.requests.RegisterStaffInvitationRequest;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationCreateResponse;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationQueryResponse;
import jakarta.validation.Valid;
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
  private final StaffInvitationQueryCommands staffInvitationQueryCommands;
  private final FrontendProperties frontendProperties;

  public StaffInvitationCommandController(
      StaffRegistrationCommands staffRegistrationCommands,
      StaffInvitationQueryCommands staffInvitationQueryCommands,
      FrontendProperties frontendProperties) {
    this.staffRegistrationCommands = staffRegistrationCommands;
    this.staffInvitationQueryCommands = staffInvitationQueryCommands;
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

  @GetMapping("/invitations/{id}")
  public ResponseEntity<StaffInvitationQueryResponse> getStaffInvitation(@PathVariable UUID id) {
    StaffInvitationQueryResult result =
        staffInvitationQueryCommands.getStaffInvitation(new StaffInvitationId(id));
    return ResponseEntity.ok(StaffInvitationQueryResponse.fromResult(result));
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
