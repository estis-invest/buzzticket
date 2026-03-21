package com.efpcode.application.usecase.partner.dto;

import com.efpcode.domain.partner.model.Partner;

public record UpdatePartnerCommand(String name, String city, String country, String isoCode) {

  public static UpdatePartnerCommand merge(UpdatePartnerCommand command, Partner partner) {
    return new UpdatePartnerCommand(
        command.name() == null || command.name().isBlank()
            ? partner.name().partnerName()
            : command.name(),
        command.city() == null || command.city().isBlank()
            ? partner.city().partnerCity()
            : command.city(),
        command.country() == null || command.country().isBlank()
            ? partner.country().partnerCountry()
            : command.country(),
        command.isoCode() == null || command.isoCode().isBlank()
            ? partner.isoCode().isoCode()
            : command.isoCode());
  }
}
