package com.efpcode.application.usecase.company.dto;

public record CompanyCommand(
    String name,
    String city,
    String country,
    String isoCode,
    String adminName,
    String adminPassword,
    String adminEmail) {}
