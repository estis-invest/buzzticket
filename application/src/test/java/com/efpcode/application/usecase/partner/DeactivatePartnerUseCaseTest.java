package com.efpcode.application.usecase.partner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerStatusTransitionException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerStatus;
import com.efpcode.domain.partner.port.PartnerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeactivatePartnerUseCaseTest {

  @Mock PartnerRepository partnerRepository;

  private DeactivatePartnerUseCase deactivatePartnerUseCase;
  private PartnerId id;
  private Partner testPartner;

  @BeforeEach
  void setUp() {
    id = PartnerId.generate();
    deactivatePartnerUseCase = new DeactivatePartnerUseCase(partnerRepository);
    testPartner = Partner.createDraftPartner(id, "Test", "Test", "TEST", "TST");
  }

  @Test
  @DisplayName("Only active partner can be deactivated all other status throws error")
  void onlyActivePartnerCanBeDeactivatedAllOtherStatusThrowsError() {

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    assertThatThrownBy(() -> deactivatePartnerUseCase.execute(id))
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(
            "PartnerStatus EDIT cannot be deactivated. Only ACTIVE PartnerStatus can transition to DEACTIVATED");

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Notfound Partner by Partner Id throws Not Found error")
  void notfoundPartnerByPartnerIdThrowsNotFoundError() {

    when(partnerRepository.findById(id)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> deactivatePartnerUseCase.execute(id))
        .isInstanceOf(PartnerNotFoundException.class)
        .hasMessageContaining("Partner not found with id:" + id.partnerId());

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Activated partner can be deactivated")
  void activatedPartnerCanBeDeactivated() {

    testPartner = testPartner.toActivate();

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    var result = deactivatePartnerUseCase.execute(id);

    assertThat(result.status()).isEqualTo(PartnerStatus.DEACTIVATED);

    verify(partnerRepository, times(1)).save(result);
  }
}
