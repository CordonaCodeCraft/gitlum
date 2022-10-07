package com.ciklum.gitlum.controller;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ControllerTest extends IntegrationTest {

	@Autowired private Controller subject;

	@Test
	@DisplayName("Given existing user produces result as expected")
	public void givenExistingUserProducesResultAsExpected() {
		// Given
		final var validRequest = new Request(EXISTING_USER);
		// When
		final var result = subject.getResult(validRequest);
		// Then
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0)).isInstanceOf(RepoDTO.class);
	}

	@Test
	@DisplayName("Given non existing user will throw 404 error")
	public void givenNonExistingUserWillThrow404Error() {
		// Given
		final var invalidRequest = new Request(NON_EXISTING_USER);
		final var expectedErrorMessage =
				String.format("404 Not Found from GET https://api.github.com/users/%s/repos", NON_EXISTING_USER);
		// Then
		assertThatThrownBy(
				() -> subject.getResult(invalidRequest)
		)
				.isInstanceOf(WebClientResponseException.class)
				.hasMessage(expectedErrorMessage);
	}

}