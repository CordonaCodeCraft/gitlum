package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetRepositoriesTest extends IntegrationTest {

	@Autowired GetRepositories subject;

	@Test
	@DisplayName("Given existing user will return non forked public repositories")
	public void givenExistingUserWillReturnNonForkedPublicRepositories() {
		// Given
		final var expectedRepositoriesCount = 1;
		// When
		final var repositories = subject.invoke(EXISTING_USER);
		final var filtersForkedRepositories = Arrays.stream(repositories).noneMatch(Utils::isForkedRepository);
		// Then
		assertThat(repositories.length).isEqualTo(expectedRepositoriesCount);
		assertThat(filtersForkedRepositories).isTrue();
	}

	@Test
	@DisplayName("Given non existing user will throw 404 error")
	public void givenNonExistingUserWillThrowAnError() {
		assertThatThrownBy(
				() -> subject.invoke(NON_EXISTING_USER)
		)
				.isInstanceOf(WebClientResponseException.class);
	}

}