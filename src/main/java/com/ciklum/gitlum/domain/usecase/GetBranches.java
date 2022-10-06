package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.git.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import static com.ciklum.gitlum.config.Constants.REPOSITORY_BRANCHES;

@UseCase
@RequiredArgsConstructor
public class GetBranches {

	private final WebClient.Builder webClientBuilder;

	public Branch[] invoke(final String userName, final String repositoryName) {
		final var branchesURI = String.format(REPOSITORY_BRANCHES, userName, repositoryName);
		return webClientBuilder
				.build()
				.get()
				.uri(branchesURI)
				.retrieve()
				.bodyToMono(Branch[].class)
				.block();
	}

}
