package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.git.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static com.ciklum.gitlum.config.Constants.USER_REPOSITORIES;

@UseCase
@RequiredArgsConstructor
public class GetRepositories {

	private final WebClient.Builder webClientBuilder;

	public Flux<Repo> invoke(final String userName) {
		final var repositoriesURI = String.format(USER_REPOSITORIES, userName);
		return  webClientBuilder
				.build()
				.get()
				.uri(repositoriesURI)
				.retrieve()
				.bodyToFlux(Repo.class)
				.filter(GetRepositories::isNotForkedRepository);
	}

	public static boolean isNotForkedRepository(final Repo repo) {
		return !repo.getFork();
	}
}
