package com.efpcode.domain.partner.model;

import java.util.Objects;

public record Partner(
    PartnerId id,
    PartnerName name,
    PartnerCity city,
    PartnerCountry country,
    PartnerIsoCode isoCode,
    PartnerStatus status,
    PartnerCreatedAt createdAt,
    PartnerUpdateAt updateAt) {

  public Partner {
    Objects.requireNonNull(id, "Id cannot be null");
    Objects.requireNonNull(name, "Name cannot be null");
    Objects.requireNonNull(city, "City cannot be null");
    Objects.requireNonNull(country, "Country cannot be null");
    Objects.requireNonNull(isoCode, "IsoCode cannot be null");
    Objects.requireNonNull(status, "Status cannot be null");
    Objects.requireNonNull(createdAt, "createdAt cannot be null");
    Objects.requireNonNull(updateAt, "updateAt cannot be null");
  }

  public boolean isActive() {
    return this.status.isActive();
  }

  public boolean isDeleted() {
    return this.status.isDeleted();
  }

  public boolean isEdit() {
    return this.status.isEdit();
  }

  public boolean canBeEdit() {
    return this.status.canBeEdit();
  }

  public boolean isDeactivated() {
    return this.status.isDeactivated();
  }

  public Partner toDeactivate() {
    return withStatus(this.status.toDeactivate(), PartnerUpdateAt.createNow());
  }

  public Partner toActivate() {
    return withStatus(this.status.toActivate(), PartnerUpdateAt.createNow());
  }

  public Partner toEdit() {
    this.status.partnerStatusEditGuard();
    return withStatus(this.status.toEdit(), PartnerUpdateAt.createNow());
  }

  public Partner toDelete() {
    return withStatus(this.status.toDelete(), PartnerUpdateAt.createNow());
  }

  public Partner updatePartner(
      PartnerName newName,
      PartnerCity newCity,
      PartnerCountry newCountry,
      PartnerIsoCode newIsoCode) {
    var editPartner = this.toEdit();

    Partner updatedPartner =
        new Partner(
            editPartner.id(),
            newName != null ? newName : editPartner.name(),
            newCity != null ? newCity : editPartner.city(),
            newCountry != null ? newCountry : editPartner.country(),
            newIsoCode != null ? newIsoCode : editPartner.isoCode(),
            editPartner.status(),
            editPartner.createdAt(),
            PartnerUpdateAt.createNow());

    return updatedPartner.toActivate();
  }

  private Partner withStatus(PartnerStatus status, PartnerUpdateAt updateAt) {
    return new Partner(
        this.id,
        this.name,
        this.city,
        this.country,
        this.isoCode,
        status,
        this.createdAt,
        updateAt);
  }

  public static Partner createDraftPartner(
      PartnerId id, String name, String city, String country, String isoCode) {

    return new Partner(
        id,
        new PartnerName(name),
        new PartnerCity(city),
        new PartnerCountry(country),
        new PartnerIsoCode(isoCode),
        PartnerStatus.EDIT,
        PartnerCreatedAt.createNow(),
        PartnerUpdateAt.createNow());
  }
}
