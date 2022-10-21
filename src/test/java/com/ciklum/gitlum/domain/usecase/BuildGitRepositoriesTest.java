package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.controllers.Request;
import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class BuildGitRepositoriesTest extends IntegrationTest {

    @Autowired BuildGitRepositories subject;

    @Test
    @DisplayName("Produces result as expected")
    public void producesResultAsExpected() {
        // Given
        final var request = new Request(FIRST_EXISTING_USER, 1, 2);
        final var expectedRepositoryCount = 1;
        final var expectedBranchCount = 12;
        final var expectedUserLogin = "Bisu-bg";
        final var expectedRepositoryName = "mobile-bg";
        final var notExpectedRepositoryName = "limeburgerapp";
        // When
        final var result = subject.invoke(request).collectList().block();
        assert result != null;
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
    }

    @Test
    @DisplayName("Given non existing user will throw 404 error")
    public void givenNonExistingUserWillThrowAnError() {
        // Given
        final var request = new Request(NON_EXISTING_USER, 1, 2);
        // Then
        assertThatThrownBy(
                () -> subject.invoke(request).blockFirst()
        )
                .isInstanceOf(WebClientResponseException.class);
    }

}