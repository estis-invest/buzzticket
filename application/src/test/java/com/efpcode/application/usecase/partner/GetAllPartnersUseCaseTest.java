package com.efpcode.application.usecase.partner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.efpcode.application.testsupport.TestUUIDIds;
import com.efpcode.application.usecase.partner.exceptions.PartnerNotFoundException;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.port.PartnerRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAllPartnersUseCaseTest {

  @Mock private PartnerRepository partnerRepository;

  private GetAllPartnersUseCase getAllPartnersUseCase;

  @BeforeEach
  void setUp() {
    getAllPartnersUseCase = new GetAllPartnersUseCase(partnerRepository);
  }

  @Test
  @DisplayName("If not partner are registered throws error")
  void ifNotPartnerAreRegisteredThrowsError() {
    when(partnerRepository.findAll()).thenReturn(List.of());

    assertThatThrownBy(() -> getAllPartnersUseCase.execute())
        .isInstanceOf(PartnerNotFoundException.class)
        .hasMessageContaining("No active partners found");
  }

  @Test
  @DisplayName("Should return a list of partners when they exist")
  void shouldReturnListOfPartners() {
    var id1 = TestUUIDIds.partnerId();
    var id2 = TestUUIDIds.partnerId();

    var partner1 = Partner.createDraftPartner(id1, "Partner A", "Stockholm", "SWEDEN", "SWE");
    var partner2 = Partner.createDraftPartner(id2, "Partner B", "Gothenburg", "SWEDEN", "SWE");

    var partnerList = List.of(partner1, partner2);

    when(partnerRepository.findAll()).thenReturn(partnerList);

    var result = getAllPartnersUseCase.execute();

    assertThat(result).hasSize(2);
    assertThat(result).containsExactlyInAnyOrder(partner1, partner2);
    verify(partnerRepository, times(1)).findAll();
  }
}
