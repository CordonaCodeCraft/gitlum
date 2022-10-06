package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.git.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import static com.ciklum.gitlum.config.Constants.USER_REPOSITORIES;

@UseCase
@RequiredArgsConstructor
public class GetRepositories {

	private final WebClient.Builder webClientBuilder;
	private final RemoveForkRepositories removeForkRepositories;

	public Repo[] invoke(final String userName) {
		final var repositoriesURI = String.format(USER_REPOSITORIES, userName);
		final var repositories = webClientBuilder
				.build()
				.get()
				.uri(repositoriesURI)
				.retrieve()
				.bodyToMono(Repo[].class)
				.block();
		return removeForkRepositories.invoke(repositories);
	}
}
