package com.efpcode.application.usecase.company.dto;

public record CompanyCommand(
    String name,
    String city,
    String country,
    String isoCode,
    String adminName,
    String adminPassword,
    String adminEmail) {
  @Override
  public String toString() {
    return "CompanyCommand{"
        + "name='"
        + name
        + '\''
        + ", city='"
        + city
        + '\''
        + ", country='"
        + country
        + '\''
        + ", isoCode='"
        + isoCode
        + '\''
        + ", adminName='"
        + adminName
        + '\''
        + ", adminPassword='"
        + "REDACTED"
        + '\''
        + ", adminEmail='"
        + "REDACTED"
        + '\''
        + '}';
  }
}
