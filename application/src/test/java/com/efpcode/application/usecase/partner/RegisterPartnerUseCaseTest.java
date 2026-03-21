package com.efpcode.application.usecase.partner;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.efpcode.application.usecase.partner.dto.RegisterPartnerCommand;
import com.efpcode.application.usecase.partner.exceptions.PartnerAlreadyExistsException;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.partner.model.PartnerName;
import com.efpcode.domain.partner.port.IdGenerator;
import com.efpcode.domain.partner.port.PartnerRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterPartnerUseCaseTest {

  @Mock private PartnerRepository partnerRepository;
  @Mock private IdGenerator idGenerator;

  private RegisterPartnerUseCase registerPartnerUseCase;

  @BeforeEach
  void setUp() {
    registerPartnerUseCase = new RegisterPartnerUseCase(partnerRepository, idGenerator);
    lenient().when(idGenerator.generate()).thenReturn(new PartnerId(UUID.randomUUID()));
  }

  @Test
  @DisplayName("Partner register successfully for unique partner name")
  void partnerRegisterSuccessfullyForUniquePartnerName() {
    var command = new RegisterPartnerCommand("Test", "Test", "TEST", "TST");
    when(partnerRepository.existsByName(any(PartnerName.class))).thenReturn(false);
    var partner = registerPartnerUseCase.execute(command);
    assertThat(partner).isNotNull();
    verify(partnerRepository).save(any());
  }

  @Test
  @DisplayName("Partner that is already registered fails uniqueness test")
  void partnerThatIsAlreadyRegisteredFailsUniquenessTest() {
    var command = new RegisterPartnerCommand("Test", "Test", "TEST", "TST");
    when(partnerRepository.existsByName(any(PartnerName.class))).thenReturn(true);

    assertThatThrownBy(() -> registerPartnerUseCase.execute(command))
        .isInstanceOf(PartnerAlreadyExistsException.class)
        .hasMessageContaining("Partner is already registered with that name: Test");

    verify(partnerRepository, never()).save(any());
  }
}
