package com.efpcode.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.efpcode.BaseIntegrationTest;
import com.efpcode.domain.partner.model.Partner;
import com.efpcode.domain.partner.model.PartnerId;
import com.efpcode.domain.user.model.*;
import com.efpcode.domain.user.port.UserRepository;
import com.efpcode.infrastructure.persistence.partner.PartnerMapper;
import com.efpcode.infrastructure.persistence.partner.SpringDataPartnerRepository;
import com.efpcode.infrastructure.persistence.user.SpringDataUserRepository;
import com.efpcode.infrastructure.security.TestSecurityConfiguration;
import com.efpcode.infrastructure.web.dto.requests.RegisterCompanyRequest;
import com.efpcode.infrastructure.web.dto.requests.RegisterPartnerRequest;
import com.efpcode.infrastructure.web.dto.requests.UpdatePartnerRequest;
import com.efpcode.infrastructure.web.dto.responses.CompanyResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@Import(TestSecurityConfiguration.class)
class PartnerControllerIT extends BaseIntegrationTest {

  @Autowired private WebTestClient webTestClient;

  @Autowired private SpringDataPartnerRepository partnerRepository;
  @Autowired private SpringDataUserRepository userDataRepository;
  @Autowired private UserRepository userRepository;

  @Nested
  @DisplayName("When connection to database is established")
  class TestConnection {
    @Test
    void contextLoads() {
      assertThat(webTestClient).isNotNull();
    }
  }

  @BeforeEach
  void clearDatabase() {
    userDataRepository.deleteAllInBatch();
    partnerRepository.deleteAllInBatch();
  }

  @Nested
  @DisplayName("When database is empty")
  class EmptyDB {
    @Test
    @DisplayName("Should return empty list when no partners exist")
    void shouldReturnEmptyList() {
      webTestClient
          .get()
          .uri("/api/v1/partners")
          .exchange()
          .expectStatus()
          .isNotFound()
          .expectHeader()
          .contentType("application/problem+json")
          .expectBody()
          .jsonPath("$.detail")
          .isEqualTo("No active partners found")
          .jsonPath("$.status")
          .isEqualTo(404);
    }
  }

  @Nested
  @DisplayName("When database is not empty")
  class NotEmptyDB {
    private UUID partnerId;

    @BeforeEach
    void setUp() {
      var companyRequest =
          new RegisterCompanyRequest(
              "Initial Partner",
              "Gothenburg",
              "SWEDEN",
              "SWE",
              "TestAdmin",
              "P@ssword123",
              "testadmin@test.com");

      partnerId =
          webTestClient
              .post()
              .uri("/public/v1/company-registration")
              .bodyValue(companyRequest)
              .exchange()
              .expectStatus()
              .isCreated()
              .expectBody(CompanyResponse.class)
              .returnResult()
              .getResponseBody()
              .partnerId();

      User admin =
          new User(
              new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001")),
              new UserName("TestAdmin"),
              new UserEmail("testadmin2@test.com"),
              UserPassword.fromHash("$2a$12$ybtImSJ.2l3C4kv4045EBOYHSfmj5MmGEdGbQVt.eEpBTGIZIgRF6"),
              UserRole.ADMIN,
              UserAccountStatus.ACTIVATED,
              UserCreatedAt.createNow(),
              UserUpdateAt.createNow(),
              Optional.of(new PartnerId(partnerId)));

      userRepository.save(admin);
    }

    @Test
    @DisplayName("Registering the same partner will cause conflict error 409")
    void registeringTheSamePartnerWillCauseConflictError409() {
      var request = new RegisterPartnerRequest("Initial Partner", "Gothenburg", "SWEDEN", "SWE");
      webTestClient
          .post()
          .uri("/api/v1/partners")
          .bodyValue(request)
          .exchange()
          .expectStatus()
          .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("Get request /api/v1/partners should return list with one item")
    void getRequestApiV1PartnersShouldReturnListWithOneItem() {
      webTestClient
          .get()
          .uri("/api/v1/partners")
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody()
          .jsonPath("$.total")
          .isEqualTo(1)
          .jsonPath("$.partners")
          .isArray()
          .jsonPath("$.partners.length()")
          .isEqualTo(1)
          .jsonPath("$.partners[0].name")
          .isEqualTo("Initial Partner");
    }

    @Test
    @DisplayName("Get request with id will return record if id matches /api/v1/partner/{id}")
    void getRequestWithIdWillReturnRecordIfIdMatchesApiV1PartnerId() {
      webTestClient
          .get()
          .uri("/api/v1/partners/{id}", partnerId)
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody()
          .jsonPath("$.name")
          .isEqualTo("Initial Partner");
    }

    @Test
    @DisplayName("Register a partner with valid entries return an ok response")
    void registerAPartnerWithValidEntriesReturnAnOkResponse() {
      var request = new RegisterPartnerRequest("Partner 100", "New York", "USA", "USA");

      webTestClient
          .post()
          .uri("/api/v1/partners")
          .bodyValue(request)
          .exchange()
          .expectStatus()
          .isCreated()
          .expectBody()
          .jsonPath("$.name")
          .isEqualTo("Partner 100")
          .jsonPath("$.city")
          .isEqualTo("New York")
          .jsonPath("$.country")
          .isEqualTo("USA")
          .jsonPath("$.isoCode")
          .isEqualTo("USA")
          .jsonPath("$.status")
          .isEqualTo("EDIT")
          .jsonPath("$.createdAt")
          .isNotEmpty()
          .jsonPath("$.updatedAt")
          .isNotEmpty()
          .jsonPath("$.id")
          .isNotEmpty();
    }

    @Test
    @DisplayName("GET count goes from one to two when registering a new partner")
    void getCountGoesFromOneToTwoWhenRegisteringANewPartner() {
      var request = new RegisterPartnerRequest("Partner 100", "New York", "USA", "USA");
      var count = partnerRepository.count();
      webTestClient
          .post()
          .uri("/api/v1/partners")
          .bodyValue(request)
          .exchange()
          .expectStatus()
          .isCreated();

      await()
          .atMost(Duration.ofSeconds(2))
          .untilAsserted(() -> assertThat(partnerRepository.count()).isEqualTo(count + 1));
    }

    @Test
    @DisplayName("PATCH /activate should transition partner from EDIT to ACTIVE")
    void shouldTransitionFromEditToActive() {

      var draftPartner =
          Partner.createDraftPartner(
              new PartnerId(UUID.randomUUID()), "Draft Partner", "Gothenburg", "SWEDEN", "SWE");

      partnerRepository.save(PartnerMapper.toEntity(draftPartner));

      UUID draftPartnerId = draftPartner.id().partnerId();

      webTestClient
          .patch()
          .uri("/api/v1/partners/{id}/activate", draftPartnerId)
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody()
          .jsonPath("$.status")
          .isEqualTo("ACTIVE")
          .jsonPath("$.updatedAt")
          .isNotEmpty();

      var updatedPartner = partnerRepository.findById(draftPartnerId).orElseThrow();
      assertThat(updatedPartner.getPartnerStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("PATCH /activate should return 404 Not Found for non-existent ID")
    void shouldReturn404WhenPartnerDoesNotExist() {
      UUID nonExistentId = UUID.randomUUID();

      webTestClient
          .patch()
          .uri("/api/v1/partners/{id}/activate", nonExistentId)
          .exchange()
          .expectStatus()
          .isNotFound()
          .expectBody()
          .jsonPath("$.status")
          .isEqualTo(404)
          .jsonPath("$.detail")
          .value(
              detail ->
                  assertThat(detail.toString())
                      .containsIgnoringCase("Partner not found with id:" + nonExistentId));
    }

    @Test
    @DisplayName("Full Lifecycle: ACTIVE -> DEACTIVATED -> DELETED")
    void shouldFollowFullStatusLifecycle() {
      // Ensure that when partner is DELETED, does not throw error PartnerNotFoundException
      var request = new RegisterPartnerRequest("Partner 100", "New York", "USA", "USA");
      webTestClient
          .post()
          .uri("/api/v1/partners")
          .bodyValue(request)
          .exchange()
          .expectStatus()
          .isCreated();

      webTestClient
          .patch()
          .uri("/api/v1/partners/{id}/deactivate", partnerId)
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody()
          .jsonPath("$.status")
          .isEqualTo("DEACTIVATED");

      webTestClient
          .delete()
          .uri("/api/v1/partners/{id}", partnerId)
          .exchange()
          .expectStatus()
          .isNoContent();

      var finalEntity = partnerRepository.findById(partnerId).orElseThrow();
      assertThat(finalEntity.getPartnerStatus()).isEqualTo("DELETED");
    }

    @Test
    @DisplayName("DELETE /id should return 422 if partner is still ACTIVE")
    void shouldReturn422WhenDeletingActivePartner() {

      webTestClient
          .delete()
          .uri("/api/v1/partners/{id}", partnerId)
          .exchange()
          .expectStatus()
          .isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT) // 422
          .expectBody()
          .jsonPath("$.detail")
          .value(
              detail ->
                  assertThat(detail.toString())
                      .contains("Only DEACTIVATED PartnerStatus can transition to DELETED"));
    }

    @Test
    @DisplayName("Update Partner: Should change name and city successfully")
    void shouldUpdatePartnerDetails() {

      var updateRequest =
          new UpdatePartnerRequest("New Amazing Name", "Stockholm", "SWEDEN", "SWE");

      webTestClient
          .patch()
          .uri("/api/v1/partners/{id}", partnerId)
          .bodyValue(updateRequest)
          .exchange()
          .expectStatus()
          .isOk()
          .expectBody()
          .jsonPath("$.name")
          .isEqualTo("New Amazing Name")
          .jsonPath("$.city")
          .isEqualTo("Stockholm");

      var updatedEntity = partnerRepository.findById(partnerId).orElseThrow();
      assertThat(updatedEntity.getPartnerName()).isEqualTo("New Amazing Name");
      assertThat(updatedEntity.getPartnerCity()).isEqualTo("Stockholm");
    }

    @Test
    @DisplayName("Update Partner: Should fail if partner id does not belong to admin")
    void updatePartnerShouldFailIfIdIsNotFound() {
      UUID someId = UUID.randomUUID();
      var invalidRequest = new UpdatePartnerRequest("", "Stockholm", "SWEDEN", "SWE");

      webTestClient
          .patch()
          .uri("/api/v1/partners/{id}", someId)
          .bodyValue(invalidRequest)
          .exchange()
          .expectStatus()
          .isEqualTo(HttpStatus.FORBIDDEN); // This triggers your Exception Handling advice!
    }

    @Test
    @DisplayName("Update Partner needs at least one value proceed")
    void updatePartnerNeedsAtLeastOneValueProceed() {
      var emptyData =
          Map.of(
              "name", "",
              "city", "",
              "country", "",
              "isoCode", "");
      webTestClient
          .patch()
          .uri("/api/v1/partners/{id}", partnerId)
          .bodyValue(emptyData)
          .exchange()
          .expectStatus()
          .isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT)
          .expectBody()
          .jsonPath("$.detail")
          .value(
              detail ->
                  assertThat(detail.toString())
                      .contains("At least one field must be provided for an update."));
    }
  }
}
