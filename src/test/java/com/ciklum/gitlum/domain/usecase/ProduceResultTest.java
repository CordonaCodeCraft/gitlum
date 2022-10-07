package com.ciklum.gitlum.domain.usecase;


import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProduceResultTest extends IntegrationTest {

	@Autowired ProduceResult subject;

	@Test
	@DisplayName("Produces result as expected")
	public void producesResultAsExpected() {
		// Given
		final var expectedRepositoryCount = 1;
		final var expectedBranchCount = 12;
		final var expectedUserLogin = "Bisu-bg";
		final var expectedRepositoryName = "mobile-bg";
		final var notExpectedRepositoryName = "limeburgerapp";
		final var expectedBranchName = "dependabot/npm_and_yarn/mobile-skeleton/node-sass-7.0.0";
		final var expectedSha = "b15402d5d1f1758925cdd3d2bc7df33a7f982f2a";
		// When
		final var result = subject.invoke(EXISTING_USER);
		final var repoDTO = result.get(0);
		final var branchDTO = repoDTO.getBranches().stream().toList().get(0);
		// Then
		assertThat(result.size()).isEqualTo(expectedRepositoryCount);

		assertThat(repoDTO).isInstanceOf(RepoDTO.class);
		assertThat(repoDTO.getRepositoryName()).isEqualTo(expectedRepositoryName);
		assertThat(repoDTO.getRepositoryName()).isNotEqualTo(notExpectedRepositoryName);
		assertThat(repoDTO.getOwnerLogin()).isEqualTo(expectedUserLogin);
		assertThat(repoDTO.getBranches().size()).isEqualTo(expectedBranchCount);

		assertThat(branchDTO).isInstanceOf(BranchDTO.class);
		assertThat(branchDTO.getBranchName()).isEqualTo(expectedBranchName);
		assertThat(branchDTO.getLastCommitSha()).isEqualTo(expectedSha);
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