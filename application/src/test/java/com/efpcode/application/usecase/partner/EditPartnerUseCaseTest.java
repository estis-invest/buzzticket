package com.efpcode.application.usecase.partner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.efpcode.application.usecase.partner.dto.UpdatePartnerCommand;
import com.efpcode.application.usecase.partner.exceptions.InvalidPartnerCommandArgumentException;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.domain.partner.exceptions.IllegalPartnerStatusTransitionException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerName;
import com.efpcode.domain.partner.port.PartnerRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EditPartnerUseCaseTest {

  @Mock private PartnerRepository partnerRepository;

  private PartnerId id;

  private Partner testPartner;

  private EditPartnerUseCase editPartnerUseCase;

  @BeforeEach
  void setUp() {
    editPartnerUseCase = new EditPartnerUseCase(partnerRepository);
    id = PartnerId.generate();
    testPartner = Partner.createDraftPartner(id, "TEST", "TEST", "TEST", "TST");
  }

  @Test
  @DisplayName("Partner name is updatable if name is unique")
  void partnerNameIsUpdatableIfNameIsUnique() {
    var expectedName = "New Test";
    var command = new UpdatePartnerCommand(expectedName, null, null, null);
    var newName = new PartnerName(expectedName);

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));
    when(partnerRepository.existsByName(newName)).thenReturn(false);

    var result = editPartnerUseCase.execute(id, command);

    assertThat(result.name().partnerName()).doesNotContain(testPartner.name().partnerName());
    assertThat(result.city().partnerCity()).contains(testPartner.city().partnerCity());
    assertThat(result.id().partnerId()).isEqualTo(testPartner.id().partnerId());

    verify(partnerRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Partner edits are not allowed if status is not edit or active for partner")
  void partnerEditsAreNotAllowedIfStatusIsNotEditOrActiveForPartner() {
    testPartner = testPartner.toActivate().toDeactivate();
    var expectedName = "New Test";
    var command = new UpdatePartnerCommand(expectedName, null, null, null);

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    assertThatThrownBy(() -> editPartnerUseCase.execute(id, command))
        .isInstanceOf(IllegalPartnerStatusTransitionException.class)
        .hasMessageContaining("PartnerStatus: DEACTIVATED cannot be edited.");

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Partner not found by id will throw error")
  void partnerNotFoundByIdWillThrowError() {
    var expectedName = "New Test";
    var command = new UpdatePartnerCommand(expectedName, null, null, null);

    when(partnerRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> editPartnerUseCase.execute(id, command))
        .isInstanceOf(InvalidPartnerCommandArgumentException.class)
        .hasMessageContaining("Partner not found with id:" + id.partnerId());

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Duplicated PartnerName is not allowed throws error if names is present")
  void duplicatedPartnerNameIsNotAllowedThrowsErrorIfNamesIsPresent() {
    var expectedName = "TEST2";
    var command = new UpdatePartnerCommand(expectedName, null, null, null);
    var duplicatedName = new PartnerName(expectedName);

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));
    when(partnerRepository.existsByName(duplicatedName)).thenReturn(true);

    assertThatThrownBy(() -> editPartnerUseCase.execute(id, command))
        .isInstanceOf(PartnerAlreadyExistsException.class)
        .hasMessageContaining("Partner name already exists: " + expectedName);

    verify(partnerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Unchanged PartnerName returns valid object")
  void unchangedPartnerNameReturnsValidObject() {

    var expectedName = "TEST";
    var command = new UpdatePartnerCommand(expectedName, null, null, null);

    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    var result = editPartnerUseCase.execute(id, command);

    assertThat(result.name().partnerName()).isEqualTo(testPartner.name().partnerName());
    verify(partnerRepository, times(1)).save(any());
  }
}
