package com.efpcode.infrastructure.persistence.partner;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "partner")
public class PartnerEntity {

  @Id
  @Column(name = "partner_id", unique = true, nullable = false)
  private UUID partnerId;

  @Column(name = "partner_name", unique = false, nullable = false)
  private String partnerName;

  @Column(name = "partner_city", nullable = false)
  private String partnerCity;

  @Column(name = "partner_country", nullable = false)
  private String partnerCountry;

  @Column(name = "partner_iso_code", nullable = false, length = 3)
  private String partnerIsoCode;

  @Column(name = "partner_status", nullable = false)
  private String partnerStatus;

  @Column(name = "partner_created_at", nullable = false)
  private Instant partnerCreatedAt;

  @Column(name = "partner_updated_at", nullable = false)
  private Instant partnerUpdatedAt;

  protected PartnerEntity() {}

  public UUID getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(UUID partnerId) {
    this.partnerId = partnerId;
  }

  public String getPartnerName() {
    return partnerName;
  }

  public void setPartnerName(String partnerName) {
    this.partnerName = partnerName;
  }

  public String getPartnerCity() {
    return partnerCity;
  }

  public void setPartnerCity(String partnerCity) {
    this.partnerCity = partnerCity;
  }

  public String getPartnerCountry() {
    return partnerCountry;
  }

  public void setPartnerCountry(String partnerCountry) {
    this.partnerCountry = partnerCountry;
  }

  public String getPartnerIsoCode() {
    return partnerIsoCode;
  }

  public void setPartnerIsoCode(String partnerIsoCode) {
    this.partnerIsoCode = partnerIsoCode;
  }

  public String getPartnerStatus() {
    return partnerStatus;
  }

  public void setPartnerStatus(String partnerStatus) {
    this.partnerStatus = partnerStatus;
  }

  public Instant getPartnerCreatedAt() {
    return partnerCreatedAt;
  }

  public void setPartnerCreatedAt(Instant partnerCreatedAt) {
    this.partnerCreatedAt = partnerCreatedAt;
  }

  public Instant getPartnerUpdatedAt() {
    return partnerUpdatedAt;
  }

  public void setPartnerUpdatedAt(Instant partnerUpdatedAt) {
    this.partnerUpdatedAt = partnerUpdatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    PartnerEntity that = (PartnerEntity) o;
    return Objects.equals(partnerId, that.partnerId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(partnerId);
  }

  @Override
  public String toString() {
    return "PartnerEntity{"
        + "partnerId="
        + partnerId
        + ", partnerName='"
        + partnerName
        + '\''
        + ", partnerCity='"
        + partnerCity
        + '\''
        + ", partnerCountry='"
        + partnerCountry
        + '\''
        + ", partnerIsoCode='"
        + partnerIsoCode
        + '\''
        + ", partnerCreatedAt="
        + partnerCreatedAt
        + ", partnerUpdatedAt="
        + partnerUpdatedAt
        + '}';
  }
}
