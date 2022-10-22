package com.ciklum.gitlum.controllers;

import com.ciklum.gitlum.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

class RoutesConfigTest extends IntegrationTest {

	@Value("${constants.git-repositories-base-url}")
	private String subjectURL;
	@Value("${constants.get-git-repositories-uri}")
	private String subjectURI;
	@Autowired private WebTestClient testClient;
	@Autowired private ApplicationContext applicationContext;

	@BeforeEach
	public void setupWebClient() {
		testClient = WebTestClient.bindToApplicationContext(applicationContext).build();
	}

	@Test
	@DisplayName("Given existing user and without pagination parameters produces result as expected")
	public void givenExistingUserAndWithoutPaginationParametersProducesResultAsExpected() {
		// given
		final var uri = String.format("/%s/%s?user=%s", subjectURL, subjectURI, FIRST_EXISTING_USER);
		final var expectedRepositoriesCount = 1;
		//then
		testClient
				.get()
				.uri(uri)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(expectedRepositoriesCount)
				.jsonPath("$[0].repositoryName").isEqualTo(FIRST_EXISTING_REPO)
				.jsonPath("$[0].ownerLogin").isEqualTo(FIRST_EXISTING_USER);
	}

	@Test
	@DisplayName("Given existing user and with pagination parameters produces result as expected")
	public void givenExistingUserAndWithPaginationParametersProducesResultAsExpected() {
		// given
		final var page = 1;
		final var resultsPerPage = 2;
		final var uri = String.format("/%s/%s?user=%s&page=%d&per_page=%d",
				subjectURL, subjectURI, SECOND_EXISTING_USER, page, resultsPerPage);
		final var expectedRepositoriesCount = 1;
		final var expectedBranchesCount = 1;
		// then
		testClient
				.get()
				.uri(uri)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(expectedRepositoriesCount)
				.jsonPath("$[0].repositoryName").isEqualTo(SECOND_EXISTING_REPO)
				.jsonPath("$[0].ownerLogin").isEqualTo(SECOND_EXISTING_USER)
				.jsonPath("$[0].branches.size()").isEqualTo(expectedBranchesCount);

	}

	@Test
	@DisplayName("Given non-existing user will return error message")
	public void givenNonExistingUserWillReturnErrorMessage() {
		// given
		final var uri = String.format("/%s/%s?user=%s", subjectURL, subjectURI, NON_EXISTING_USER);
		final var expectedStatusCode = "404";
		final var expectedMessage = "Github user not found";
		//then
		testClient
				.get()
				.uri(uri)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("httpStatus").isEqualTo(expectedStatusCode)
				.jsonPath("message").isEqualTo(expectedMessage);
	}

	@Test
	@DisplayName("Given invalid header will return error message")
	public void givenInvalidHeaderWillReturnErrorMessage() {
		// given
		final var uri = String.format("/%s/%s?user=%s", subjectURL, subjectURI, FIRST_EXISTING_USER);
		final var expectedStatusCode = "406";
		final var expectedMessage = "Invalid MediaType";
		//then
		testClient
				.get()
				.uri(uri)
				.header("Accept", "application/xml")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("httpStatus").isEqualTo(expectedStatusCode)
				.jsonPath("message").isEqualTo(expectedMessage);
	}

}