package com.efpcode.domain.partner.model;

public record Partner(
    PartnerId id,
    PartnerName name,
    PartnerCity city,
    PartnerIsoCode isoCode,
    PartnerStatus status) {}
