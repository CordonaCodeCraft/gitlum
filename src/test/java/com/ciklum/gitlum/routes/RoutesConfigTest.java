package com.ciklum.gitlum.routes;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.config.EndpointProperties;
import com.ciklum.gitlum.config.RequestProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

class RoutesConfigTest extends IntegrationTest {

  @Autowired private EndpointProperties endpointProperties;
  @Autowired private RequestProperties requestProperties;
  @Autowired private WebTestClient testClient;
  @Autowired private ApplicationContext applicationContext;

  @BeforeEach
  public void beforeEach() {
    testClient = WebTestClient.bindToApplicationContext(applicationContext).build();
  }

  @Test
  @DisplayName("Given existing user and without pagination parameters produces result as expected")
  public void givenExistingUserAndWithoutPaginationParametersProducesResultAsExpected() {
    // given
    final var uri =
        String.format(
            "/%s/%s?user=%s",
            endpointProperties.getBaseUrl(),
            endpointProperties.getGetRepositories(),
            FIRST_EXISTING_USER);
    final var expectedRepositoriesCount = 1;
    // then
    testClient
        .get()
        .uri(uri)
        .header(
            requestProperties.getAuthorizationHeaderKey(),
            requestProperties.getTokenPrefix() + TOKEN)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.length()")
        .isEqualTo(expectedRepositoriesCount)
        .jsonPath("$[0].repositoryName")
        .isEqualTo(FIRST_EXISTING_REPO)
        .jsonPath("$[0].ownerLogin")
        .isEqualTo(FIRST_EXISTING_USER);
  }

  @Test
  @DisplayName("Given existing user and with pagination parameters produces result as expected")
  public void givenExistingUserAndWithPaginationParametersProducesResultAsExpected() {
    // given
    final var page = 1;
    final var resultsPerPage = 2;
    final var uri =
        String.format(
            "/%s/%s?user=%s&page=%d&per_page=%d",
            endpointProperties.getBaseUrl(),
            endpointProperties.getGetRepositories(),
            SECOND_EXISTING_USER,
            page,
            resultsPerPage);
    final var expectedRepositoriesCount = 1;
    final var expectedBranchesCount = 1;
    // then
    testClient
        .get()
        .uri(uri)
        .header(
            requestProperties.getAuthorizationHeaderKey(),
            requestProperties.getTokenPrefix() + TOKEN)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.length()")
        .isEqualTo(expectedRepositoriesCount)
        .jsonPath("$[0].repositoryName")
        .isEqualTo(SECOND_EXISTING_REPO)
        .jsonPath("$[0].ownerLogin")
        .isEqualTo(SECOND_EXISTING_USER)
        .jsonPath("$[0].branches.size()")
        .isEqualTo(expectedBranchesCount);
  }

  @Test
  @DisplayName("Given non-existing user will return 404 error message")
  public void givenNonExistingUserWillReturn404ErrorMessage() {
    // given
    final var uri =
        String.format(
            "/%s/%s?user=%s",
            endpointProperties.getBaseUrl(),
            endpointProperties.getGetRepositories(),
            NON_EXISTING_USER);
    final var expectedStatusCode = "404";
    final var expectedMessage = String.format("Github user %s not found", NON_EXISTING_USER);
    // then
    testClient
        .get()
        .uri(uri)
        .header(
            requestProperties.getAuthorizationHeaderKey(),
            requestProperties.getTokenPrefix() + TOKEN)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("httpStatus")
        .isEqualTo(expectedStatusCode)
        .jsonPath("message")
        .isEqualTo(expectedMessage);
  }

  @Test
  @DisplayName("Given user not provided will return 400 error")
  public void givenUserNotProvidedWillReturn400ErrorMessage() {
    // given
    final var uri =
        String.format(
            "/%s/%s", endpointProperties.getBaseUrl(), endpointProperties.getGetRepositories());
    final var expectedStatusCode = "400";
    final var expectedMessage = "Github user not provided";
    // then
    testClient
        .get()
        .uri(uri)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("httpStatus")
        .isEqualTo(expectedStatusCode)
        .jsonPath("message")
        .isEqualTo(expectedMessage);
  }

  @ParameterizedTest(name = "({index}) Invalid media type = ''{0}''")
  @ValueSource(strings = {"application/xml"})
  @DisplayName("Given invalid media type will return 406 error message")
  public void givenInvalidMediaTypeWillReturn406ErrorMessage(final String invalidMediaType) {
    // given
    final var uri =
        String.format(
            "/%s/%s?user=%s",
            endpointProperties.getBaseUrl(),
            endpointProperties.getGetRepositories(),
            FIRST_EXISTING_USER);
    final var expectedStatusCode = "406";
    final var expectedMessage = String.format("Invalid media type: %s", invalidMediaType);
    // then
    testClient
        .get()
        .uri(uri)
        .header(
            requestProperties.getAuthorizationHeaderKey(),
            requestProperties.getTokenPrefix() + TOKEN)
        .header("Accept", invalidMediaType)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("httpStatus")
        .isEqualTo(expectedStatusCode)
        .jsonPath("message")
        .isEqualTo(expectedMessage);
  }

  @Test
  @DisplayName("Given wrong credentials will return 401 error message")
  public void givenWrongCredentialsWillReturn401ErrorMessage() {
    // given
    final var invalidToken = "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sdsfdfdsdsddsfsdsdsdf";
    final var uri =
        String.format(
            "/%s/%s?user=%s",
            endpointProperties.getBaseUrl(),
            endpointProperties.getGetRepositories(),
            FIRST_EXISTING_USER);
    final var expectedStatusCode = "401";
    final var expectedMessage = "Wrong credentials";
    // then
    testClient
        .get()
        .uri(uri)
        .header(
            requestProperties.getAuthorizationHeaderKey(),
            requestProperties.getTokenPrefix() + invalidToken)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("httpStatus")
        .isEqualTo(expectedStatusCode)
        .jsonPath("message")
        .isEqualTo(expectedMessage);
  }
}
