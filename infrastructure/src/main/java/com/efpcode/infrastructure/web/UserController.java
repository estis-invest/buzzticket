package com.efpcode.infrastructure.web;

import com.efpcode.application.port.context.RequestContext;
import com.efpcode.application.usecase.user.RegisterStaffUseCase;
import com.efpcode.application.usecase.user.dto.RegisterStaffCommand;
import com.efpcode.domain.user.model.User;
import com.efpcode.infrastructure.web.dto.RegisterStaffRequest;
import com.efpcode.infrastructure.web.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
class UserController {

  private final RegisterStaffUseCase registerStaffUseCase;
  private final RequestContext requestContext;

  public UserController(RequestContext requestContext, RegisterStaffUseCase registerStaffUseCase) {
    this.requestContext = requestContext;
    this.registerStaffUseCase = registerStaffUseCase;
  }

  @PostMapping("/staff")
  public ResponseEntity<UserResponse> registerStaff(
      @Valid @RequestBody RegisterStaffRequest request) {
    var command =
        new RegisterStaffCommand(
            request.name(), request.email(), request.password(), request.role());

    User staff = registerStaffUseCase.execute(requestContext, command);

    return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(staff));
  }
}
