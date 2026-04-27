package com.efpcode.application.usecase.user.dto;

import com.efpcode.application.usecase.user.exceptions.InvalidUserCommandArgumentException;

public record RegisterUserWithPartnerCommand(
    String userName, String userEmail, String userPassword, String userPartner) {

  public RegisterUserWithPartnerCommand {
    validateRequiredFields(userName, "Username");
    validateRequiredFields(userEmail, "User email");
    validateRequiredFields(userPassword, "User password");
    validateRequiredFields(userPartner, "User Partner");
  }

  private void validateRequiredFields(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new InvalidUserCommandArgumentException(
          String.format("%s is required and cannot be null or blank", fieldName));
    }
  }
}
