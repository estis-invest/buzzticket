package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.StaffInvitationAcceptanceCommands;
import com.efpcode.application.usecase.user.dto.RegisterStaffInvitationAccountCommand;
import com.efpcode.application.usecase.user.dto.StaffInvitationAcceptanceResult;
import com.efpcode.infrastructure.web.dto.requests.StaffInvitationAcceptanceRequest;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationAcceptanceResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/staff/invitations/accept")
class StaffInvitationAcceptanceController {

  private final StaffInvitationAcceptanceCommands staffInvitationAcceptanceCommands;

  StaffInvitationAcceptanceController(StaffInvitationAcceptanceCommands staffRegistrationCommands) {
    this.staffInvitationAcceptanceCommands = staffRegistrationCommands;
  }

  @PostMapping
  public ResponseEntity<StaffInvitationAcceptanceResponse> acceptInvite(
      @Valid @RequestBody StaffInvitationAcceptanceRequest request) {
    var command =
        new RegisterStaffInvitationAccountCommand(
            request.rawToken(), request.name(), request.password());

    StaffInvitationAcceptanceResult result = staffInvitationAcceptanceCommands.acceptInvitation(command);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(StaffInvitationAcceptanceResponse.fromResult(result));
  }
}
