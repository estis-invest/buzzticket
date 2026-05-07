package com.efpcode.infrastructure.web;

import com.efpcode.application.context.RequestContext;
import com.efpcode.application.port.in.auth.AuthenticatedSession;
import com.efpcode.application.usecase.auth.GetLoginUseCase;
import com.efpcode.application.usecase.auth.dto.AuthResult;
import com.efpcode.application.usecase.auth.dto.RegisterLoginCommand;
import com.efpcode.infrastructure.web.dto.AuthResponse;
import com.efpcode.infrastructure.web.dto.AuthenticatedUserResponse;
import com.efpcode.infrastructure.web.dto.RegisterLoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
class AuthController {

  private final GetLoginUseCase getLoginUseCase;
  private final AuthenticatedSession authenticatedSession;
  private final RequestContext requestContext;

  AuthController(
      GetLoginUseCase getLoginUseCase,
      AuthenticatedSession authenticatedSession,
      RequestContext requestContext) {
    this.getLoginUseCase = getLoginUseCase;
    this.authenticatedSession = authenticatedSession;
    this.requestContext = requestContext;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> getLogin(@Valid @RequestBody RegisterLoginRequest request) {

    var command = new RegisterLoginCommand(request.email(), request.password());

    AuthResult result = getLoginUseCase.execute(command);

    return ResponseEntity.ok(AuthResponse.from(result));
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponse> refresh() {

    AuthResult result = authenticatedSession.refresh(requestContext);
    return ResponseEntity.status(HttpStatus.OK).body(AuthResponse.from(result));
  }

  @GetMapping("/me")
  public ResponseEntity<AuthenticatedUserResponse> me() {
    return ResponseEntity.ok(
        AuthenticatedUserResponse.from(authenticatedSession.me(requestContext)));
  }
}
