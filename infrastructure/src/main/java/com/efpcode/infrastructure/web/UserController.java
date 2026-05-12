package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.dto.CreateStaffInvitationResult;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationCommand;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.config.properties.FrontendProperties;
import com.efpcode.infrastructure.web.dto.requests.RegisterStaffInvitationRequest;
import com.efpcode.infrastructure.web.dto.requests.RegisterStaffRequest;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationResponse;
import com.efpcode.infrastructure.web.dto.responses.UserResponse;
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
@RequestMapping("/api/v1/users")
class UserController {

  private final StaffRegistrationCommands staffRegistrationCommands;
  private final FrontendProperties frontendProperties;

  public UserController(
      StaffRegistrationCommands staffRegistrationCommands, FrontendProperties frontendProperties) {
    this.staffRegistrationCommands = staffRegistrationCommands;
    this.frontendProperties = frontendProperties;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/staff")
  public ResponseEntity<UserResponse> registerStaff(
      @Valid @RequestBody RegisterStaffRequest request) {
    var command =
        new RegisterStaffCommand(
            request.name(), request.email(), request.password(), request.role());

    User staff = staffRegistrationCommands.register(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(staff));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/staff/invitations")
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
    var tokenStem = "/staff/invitations/accept?token=";
    return UriComponentsBuilder.fromUriString(frontendProperties.baseUrl())
        .path(tokenStem)
        .queryParam(token)
        .build()
        .toUriString();
  }
}
