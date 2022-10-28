package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.domain.model.git.RepoOwner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestPropertySource(properties = "gitlum.git.max-results-per-page-for-branches-allowed=1")
public class BranchPaginationTests extends IntegrationTest {

  @Autowired GetBranches subject;

  @Test
  @DisplayName("Given multiple paginated results will return branches count as expected")
  public void givenMultiplePaginatedResultsWillReturnBranchesCountAsExpected() {
    // Given
    final var existingUser = new RepoOwner(FIRST_EXISTING_USER);
    final var existingRepo = new Repo(FIRST_EXISTING_REPO, existingUser);
    final var expectedBranchesCount = 12;
    // When
    final var branches = subject.invoke(existingRepo, TOKEN).collectList().block();
    // Then
    assert branches != null;
    assertThat(branches.size()).isEqualTo(expectedBranchesCount);
  }
}
