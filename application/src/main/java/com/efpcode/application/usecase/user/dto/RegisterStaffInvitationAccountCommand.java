package com.efpcode.application.usecase.user.dto;

public record RegisterStaffInvitationAccountCommand(
    String rawToken, String name, String password) {}
