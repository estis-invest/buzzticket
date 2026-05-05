package com.efpcode.infrastructure.web;

import com.efpcode.application.usecase.auth.GetLoginUseCase;
import com.efpcode.application.usecase.auth.dto.AuthResult;
import com.efpcode.application.usecase.auth.dto.RegisterLoginCommand;
import com.efpcode.infrastructure.web.dto.AuthResponse;
import com.efpcode.infrastructure.web.dto.RegisterLoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
class AuthController {

  private final GetLoginUseCase getLoginUseCase;

  AuthController(GetLoginUseCase getLoginUseCase) {
    this.getLoginUseCase = getLoginUseCase;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> getLogin(@Valid @RequestBody RegisterLoginRequest request) {

    var command = new RegisterLoginCommand(request.email(), request.password());

    AuthResult result = getLoginUseCase.execute(command);

    return ResponseEntity.ok(AuthResponse.from(result));
  }
}
