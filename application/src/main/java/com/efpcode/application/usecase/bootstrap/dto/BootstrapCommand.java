package com.efpcode.application.usecase.bootstrap.dto;

public record BootstrapCommand(
    String name,
    String city,
    String country,
    String isoCode,
    String adminName,
    String adminPassword,
    String adminEmail) {}
