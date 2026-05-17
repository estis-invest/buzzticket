package com.efpcode.infrastructure.web;

import com.efpcode.application.port.in.user.AdminAccountCommands;
import com.efpcode.application.port.in.user.AdminUserReads;
import com.efpcode.application.port.in.user.StaffRegistrationCommands;
import com.efpcode.application.port.in.user.UserAccountCommands;
import com.efpcode.application.usecase.user.dto.*;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.web.dto.requests.*;
import com.efpcode.infrastructure.web.dto.responses.UserListResponse;
import com.efpcode.infrastructure.web.dto.responses.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
class UserController {

  private final StaffRegistrationCommands staffRegistrationCommands;
  private final UserAccountCommands userAccountCommands;
  private final AdminAccountCommands adminAccountCommands;
  private final AdminUserReads adminUserReads;

  public UserController(
      StaffRegistrationCommands staffRegistrationCommands,
      UserAccountCommands userAccountCommands,
      AdminAccountCommands adminAccountCommands,
      AdminUserReads adminUserReads) {
    this.staffRegistrationCommands = staffRegistrationCommands;
    this.userAccountCommands = userAccountCommands;
    this.adminAccountCommands = adminAccountCommands;
    this.adminUserReads = adminUserReads;
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

  @DeleteMapping("/me")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAccount(@Valid @RequestBody RegisterAccountDeactivationRequest request) {
    var command = new UserAccountDeactivationCommand(request.currentPassword());

    userAccountCommands.deactivateAccount(command);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{id}/deactivate")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deactivateAccount(@PathVariable UUID id) {
    var command = new DeactivateCommand(id);
    adminAccountCommands.deactivateAccount(command);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{id}/activate")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void activateAccount(@PathVariable UUID id) {
    var command = new ActivateUserCommand(id);
    adminAccountCommands.activateAccount(command);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{id}/promote")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void promoteAccount(@PathVariable UUID id) {
    var command = new PromoteCommand(id);
    adminAccountCommands.promoteUser(command);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/{id}/demote")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void demoteAccount(@PathVariable UUID id) {
    var command = new DemoteCommand(id);
    adminAccountCommands.demoteUser(command);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<UserListResponse> getAllUsers() {

    List<UserResult> userResults = adminUserReads.getUsers();
    List<UserResponse> userListResponses =
        userResults.stream().map(UserResponse::fromResult).toList();

    return ResponseEntity.ok(new UserListResponse(userListResponses, userListResponses.size()));
  }

  // TODO: Admin user management endpoints (future work)
  // - GET  /api/v1/users
  //
  // Notes:
  // - These are admin-only operations (role-based authorization required)
  // - Consider separate AdminUserCommands/use cases (do NOT reuse self `/me` flows)
  // - Define clear domain rules for role transitions and activation lifecycle
  // - Ensure audit logging for all actions

}
