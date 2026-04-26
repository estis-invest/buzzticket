package com.efpcode.application.usecase.partner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.efpcode.application.testsupport.TestUUIDIds;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
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
class GetPartnerUseCaseTest {

  @Mock private PartnerRepository partnerRepository;

  private PartnerId id;

  private Partner testPartner;

  private GetPartnerUseCase getPartnerUseCase;

  @BeforeEach
  void setUp() {

    getPartnerUseCase = new GetPartnerUseCase(partnerRepository);
    id = TestUUIDIds.partnerId();
    testPartner = Partner.createDraftPartner(id, "TEST", "TEST", "TEST", "TST");
  }

  @Test
  @DisplayName("Partner not found with id throws error")
  void partnerNotFoundWithIdThrowsError() {

    when(partnerRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> getPartnerUseCase.execute(id))
        .isInstanceOf(PartnerNotFoundException.class)
        .hasMessageContaining("Partner was not found: " + id.partnerId());
  }

  @Test
  @DisplayName("Partner found by id returns valid object")
  void partnerFoundByIdReturnsValidObject() {
    when(partnerRepository.findById(id)).thenReturn(Optional.of(testPartner));

    var results = getPartnerUseCase.execute(id);

    assertThat(results).isEqualTo(testPartner);
    verify(partnerRepository, times(1)).findById(id);
  }
}
