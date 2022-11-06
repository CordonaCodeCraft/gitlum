package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.IntegrationTest;
import com.ciklum.gitlum.domain.model.git.Repo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetBranchesTest extends IntegrationTest {

  @Autowired GetBranches subject;

  @Test
  @DisplayName("Given existing repository ID returns branches as expected")
  public void givenExistingRepositoryIdReturnsBranchesAsExpected() {
    // Given
    final var existingRepo = new Repo(FIRST_EXISTING_REPO_ID);
    final var expectedBranchesCount = 12;
    // When
    final var branches = subject.invoke(existingRepo, NO_AUTH_TOKEN).collectList().block();
    // Then
    assert branches != null;
    assertThat(branches.size()).isEqualTo(expectedBranchesCount);
  }
}
