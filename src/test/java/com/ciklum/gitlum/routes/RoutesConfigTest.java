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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@TestPropertySource(properties = "gitlum.request.default-results-per-page=3")
class RoutesConfigTest extends IntegrationTest {

  @Autowired private EndpointProperties endpointProperties;
  @Autowired private RequestProperties requestProperties;
  @Autowired private WebTestClient testClient;
  @Autowired private ApplicationContext applicationContext;

  @BeforeEach
  void bindClientToApplicationContext() {
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
            requestProperties.getTokenPrefix() + NO_AUTH_TOKEN)
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
            requestProperties.getTokenPrefix() + NO_AUTH_TOKEN)
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
            requestProperties.getTokenPrefix() + NO_AUTH_TOKEN)
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
}
