package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.controllers.Request;
import com.ciklum.gitlum.domain.model.git.Repo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class GetRepositoriesTest extends IntegrationTest {

	@Autowired GetRepositories subject;

	@Test
	@DisplayName("Given existing user will return non forked public repositories")
	public void givenExistingUserWillReturnNonForkedPublicRepositories() {
		// Given
		final var expectedRepositoriesCount = 1;
		final var validRequest = new Request(FIRST_EXISTING_USER, 1, 3, TOKEN);
		// When
		final var repositories = subject
				.invoke(validRequest)
				.filter(this::isNotForkedRepository)
				.collectList()
				.block();
		assert repositories != null;
		final var filtersForkedRepositories = repositories.stream().noneMatch(this::isForkedRepository);
		// Then
		assertThat(repositories.size()).isEqualTo(expectedRepositoriesCount);
		assertThat(filtersForkedRepositories).isTrue();
	}

	@Test
	@DisplayName("Given non existing user will throw 404 error")
	public void givenNonExistingUserWillThrowAnError() {
		// Given
		final var invalidRequest = new Request(NON_EXISTING_USER, 1, 3, TOKEN);
		// Then
		assertThatThrownBy(
				() -> subject
						.invoke(invalidRequest)
						.blockFirst()
		)
				.isInstanceOf(WebClientResponseException.NotFound.class);
	}

	private boolean isNotForkedRepository(final Repo repo) {
		return !repo.getFork();
	}

	private boolean isForkedRepository(final Repo repo) {
		return repo.getFork();
	}

}