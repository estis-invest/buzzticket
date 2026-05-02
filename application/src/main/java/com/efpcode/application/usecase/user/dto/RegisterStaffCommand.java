package com.efpcode.application.usecase.user.dto;

public record RegisterStaffCommand(String name, String email, String password, String role) {}
