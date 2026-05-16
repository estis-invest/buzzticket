package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.port.in.user.UserAccountCommands;
import com.efpcode.application.usecase.user.dto.*;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.web.dto.requests.RegisterStaffRequest;
import com.efpcode.infrastructure.web.dto.requests.RegisterUserEmailChangeRequest;
import com.efpcode.infrastructure.web.dto.requests.RegisterUserNameChangeRequest;
import com.efpcode.infrastructure.web.dto.requests.RegisterUserPasswordChangeRequest;
import com.efpcode.infrastructure.web.dto.responses.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
class UserController {

  private final StaffRegistrationCommands staffRegistrationCommands;
  private final UserAccountCommands userAccountCommands;

  public UserController(
      StaffRegistrationCommands staffRegistrationCommands,
      UserAccountCommands userAccountCommands) {
    this.staffRegistrationCommands = staffRegistrationCommands;
    this.userAccountCommands = userAccountCommands;
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

  @PatchMapping("/me/name")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUserName(@Valid @RequestBody RegisterUserNameChangeRequest request) {

    var command = new ChangeUserNameCommand(request.name());
    userAccountCommands.updateUserName(command);
  }

  @PatchMapping("/me/email")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateEmail(@Valid @RequestBody RegisterUserEmailChangeRequest request) {
    var command = new ChangeUserEmailCommand(request.email());
    userAccountCommands.updateUserEmail(command);
  }

  @PatchMapping("/me/password")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updatePassword(@Valid @RequestBody RegisterUserPasswordChangeRequest request) {
    var command = new ChangeUserPasswordCommand(request.currentPassword(), request.newPassword());

    userAccountCommands.updateUserPassword(command);
  }
}
