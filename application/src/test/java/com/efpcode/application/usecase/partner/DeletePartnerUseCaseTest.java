package com.efpcode.application.usecase.partner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.efpcode.application.testsupport.TestUUIDIds;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerStatusTransitionException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.port.PartnerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeletePartnerUseCaseTest {

  @Mock private PartnerRepository partnerRepository;

  private DeletePartnerUseCase deletePartnerUseCase;

  private PartnerId id;

  private Partner testPartner;

  @BeforeEach
  void setUp() {
    deletePartnerUseCase = new DeletePartnerUseCase(partnerRepository);
    id = TestUUIDIds.partnerId();
    testPartner = Partner.createDraftPartner(id, "TEST", "TEST", "TEST", "TST");
  }

  @Test
  @DisplayName("Only deactivated partners can transition to Deleted status else throws error")
  void onlyDeactivatedPartnersCanTransitionToDeletedStatusElseThrowsError() {

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    assertThatThrownBy(() -> deletePartnerUseCase.execute(id))
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining(
            "PartnerStatus EDIT cannot be deleted. Only DEACTIVATED PartnerStatus can transition to DELETED");

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Partner not found by id will throw error")
  void partnerNotFoundByIdWillThrowError() {
    when(partnerRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> deletePartnerUseCase.execute(id))
        .isInstanceOf(PartnerNotFoundException.class)
        .hasMessageContaining("Partner not found with id: " + id.partnerId());

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Partner that have status Deactivated can transition to Deleted status")
  void partnerThatHaveStatusDeactivatedCanTransitionToDeletedStatus() {
    testPartner = testPartner.toActivate().toDeactivate();

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    deletePartnerUseCase.execute(id);

    verify(partnerRepository, times(1)).save(any());
  }
}
