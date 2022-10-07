package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.domain.model.git.Repo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import static com.ciklum.gitlum.config.Constants.USER_REPOSITORIES;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RemoveForkRepositoriesTest extends IntegrationTest {

	@Autowired private RemoveForkRepositories subject;
	@Autowired private WebClient.Builder webClientBuilder;

	@Test
	@DisplayName("Removes forked repositories")
	public void removesForkedRepositories() {
		// Given
		final var expectedRepositoriesCount = 1;
		final var repositoriesURI = String.format(USER_REPOSITORIES, EXISTING_USER);
		final var repositories = webClientBuilder
				.build()
				.get()
				.uri(repositoriesURI)
				.retrieve()
				.bodyToMono(Repo[].class)
				.block();
		// When
		final var filteredRepositories = subject.invoke(repositories);
		// Then
		assertThat(filteredRepositories.length).isEqualTo(expectedRepositoriesCount);

	}
}