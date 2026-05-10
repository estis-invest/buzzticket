package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.web.dto.requests.RegisterStaffRequest;
import com.efpcode.infrastructure.web.dto.responses.StaffInvitationResponse;
import com.efpcode.infrastructure.web.dto.responses.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
class UserController {

  private final StaffRegistrationCommands staffRegistrationCommands;

  public UserController(StaffRegistrationCommands staffRegistrationCommands) {
    this.staffRegistrationCommands = staffRegistrationCommands;
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

  @PostMapping("/staff/invitations")
  public ResponseEntity<StaffInvitationResponse> sendInvite() {
    return null;
  }
}
