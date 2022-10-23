package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.domain.model.git.RepoOwner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


class GetBranchesTest extends IntegrationTest {


	@Autowired GetBranches subject;

	@Test
	@DisplayName("Given existing user and repositories returns branches as expected")
	public void givenExistingUserAndRepositoryReturnsBranchesAsExpected() {
		// Given
		final var existingUser = new RepoOwner(FIRST_EXISTING_USER);
		final var existingRepo = new Repo(FIRST_EXISTING_REPO, existingUser);
		final var expectedBranchesCount = 12;
		// When
		final var branches = subject.invoke(existingRepo, VALID_TOKEN).collectList().block();
		// Then
		assert branches != null;
		assertThat(branches.size()).isEqualTo(expectedBranchesCount);
	}

	@Test
	@DisplayName("Given non existing repo will throw 404 error")
	public void givenNonExistingRepoWillThrowAnError() {
		// Given
		final var existingUser = new RepoOwner(FIRST_EXISTING_USER);
		final var nonExistingRepo = new Repo(NON_EXISTING_REPO, existingUser);
		// Then
		assertThatThrownBy(
				() -> subject.invoke(nonExistingRepo, VALID_TOKEN).blockFirst()
		)
				.isInstanceOf(WebClientResponseException.NotFound.class);
	}

	@Test
	@DisplayName("Given non existing user will throw 404 error")
	public void givenNonExistingUserWillThrowAnError() {
		// Given
		final var nonExistingUser = new RepoOwner(NON_EXISTING_USER);
		final var existingRepo = new Repo(FIRST_EXISTING_REPO, nonExistingUser);
		// Then
		assertThatThrownBy(
				() -> subject.invoke(existingRepo, VALID_TOKEN).blockFirst()
		)
				.isInstanceOf(WebClientResponseException.NotFound.class);
	}

}