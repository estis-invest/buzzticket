package com.efpcode.infrastructure.web.dto;

import com.efpcode.application.usecase.auth.dto.AuthResult;

public record AuthResponse(String token) {

  public static AuthResponse from(AuthResult result) {
    return new AuthResponse(result.token());
  }
}
