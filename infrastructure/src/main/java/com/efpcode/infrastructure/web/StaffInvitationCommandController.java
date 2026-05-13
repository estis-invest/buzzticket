package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.dto.CreateStaffInvitationResult;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationCommand;
import com.efpcode.infrastructure.config.properties.FrontendProperties;
import com.efpcode.infrastructure.web.dto.requests.RegisterStaffInvitationRequest;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationResponse;
import jakarta.validation.Valid;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/staff")
class StaffInvitationCommandController {

  private final StaffRegistrationCommands staffRegistrationCommands;
  private final FrontendProperties frontendProperties;

  public StaffInvitationCommandController(
      StaffRegistrationCommands staffRegistrationCommands, FrontendProperties frontendProperties) {
    this.staffRegistrationCommands = staffRegistrationCommands;
    this.frontendProperties = frontendProperties;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/invitations")
  public ResponseEntity<StaffInvitationResponse> sendInvite(
      @Valid @RequestBody RegisterStaffInvitationRequest request) {
    var command =
        new RegisterStaffInvitationCommand(
            request.inviteeEmail(), request.role().toUpperCase(Locale.ROOT), request.expiresAt());

    CreateStaffInvitationResult invitationResult =
        staffRegistrationCommands.sendInvitation(command);
    String inviteLink = generateLink(invitationResult.rawToken());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new StaffInvitationResponse(
                invitationResult.invitationId(),
                invitationResult.inviteeEmail(),
                invitationResult.role(),
                invitationResult.status(),
                invitationResult.expiresAt(),
                inviteLink));
  }

  private String generateLink(String token) {
    var tokenStem = "public/staff/invitations/accept";
    return UriComponentsBuilder.fromUriString(frontendProperties.baseUrl())
        .path(tokenStem)
        .queryParam("token", token)
        .build()
        .toUriString();
  }
}
