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
class ActivatePartnerUseCaseTest {
  @Mock private PartnerRepository partnerRepository;

  private ActivatePartnerUseCase activatePartnerUseCase;

  @BeforeEach
  void setUp() {
    activatePartnerUseCase = new ActivatePartnerUseCase(partnerRepository);
  }

  @Test
  @DisplayName("should successfully activate an existing partner from EDIT to ACITVE")
  void shouldSuccessfullyActivateAnExistingPartnerFromEditToActive() {
    var id = PartnerId.generate();
    var testPartner = Partner.createDraftPartner(id, "Test", "TEST", "TEST", "TST");

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    Partner result = activatePartnerUseCase.execute(id);
    assertThat(result.status()).isEqualTo(PartnerStatus.ACTIVE);

    verify(partnerRepository, times(1)).save(result);
  }

  @Test
  @DisplayName("not found partnerId throws error")
  void notFoundPartnerIdThrowsError() {

    var id = PartnerId.generate();

    when(partnerRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> activatePartnerUseCase.execute(id))
        .isInstanceOf(PartnerNotFoundException.class)
        .hasMessageContaining("Partner not found with id:" + id.partnerId());

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Cannot activate partner in deleted  status")
  void cannotActivatePartnerInDeletedStatus() {
    var id = PartnerId.generate();
    var testPartner = Partner.createDraftPartner(id, "Test", "TEST", "TEST", "TST");

    testPartner = testPartner.toActivate().toDeactivate().toDelete();

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    assertThatThrownBy(() -> activatePartnerUseCase.execute(id))
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(
            "PartnerStatus DELETED cannot be activated. Only DEACTIVATED and EDIT PartnerStatus can transition to ACTIVE");

    verify(partnerRepository, never()).save(any());
  }
}
