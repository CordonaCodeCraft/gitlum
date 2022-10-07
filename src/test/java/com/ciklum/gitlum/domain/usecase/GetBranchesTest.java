package com.ciklum.gitlum.domain.usecase;


import com.ciklum.gitlum.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetBranchesTest extends IntegrationTest {

	@Autowired GetBranches subject;

	@Test
	@DisplayName("Given existing user and repositories returns branches as expected")
	public void givenExistingUserAndRepositoryReturnsBranchesAsExpected() {
		// Given
		final var expectedBranchesCount = 12;
		// When
		final var branches = subject.invoke(EXISTING_USER, EXISTING_REPO);
		// Then
		assertThat(branches.length).isEqualTo(expectedBranchesCount);
	}

	@Test
	@DisplayName("Given non existing repo will throw 404 error")
	public void givenNonExistingRepoWillThrowAnError() {
		assertThatThrownBy(
				() -> subject.invoke(EXISTING_USER, NON_EXISTING_REPO)
		)
				.isInstanceOf(WebClientResponseException.class);
	}

	@Test
	@DisplayName("Given non existing user will throw 404 error")
	public void givenNonExistingUserWillThrowAnError() {
		assertThatThrownBy(
				() -> subject.invoke(NON_EXISTING_USER, EXISTING_REPO)
		)
				.isInstanceOf(WebClientResponseException.class);
	}

}