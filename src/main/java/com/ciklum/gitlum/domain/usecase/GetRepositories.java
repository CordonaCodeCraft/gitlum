package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.config.GitProperties;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.routes.dto.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@UseCase
@RequiredArgsConstructor
public class GetRepositories {

	private final GitProperties gitProperties;
	private final WebClient.Builder webClientBuilder;

	public Flux<Repo> invoke(final Request request) {
		return webClientBuilder
				.baseUrl(gitProperties.getBaseUrl())
				.build()
				.get()
				.uri(uriBuilder ->
						uriBuilder
								.path(gitProperties.getRepositoriesUri())
								.queryParam("page", request.pageNumber())
								.queryParam("per_page", request.resultsPerPage())
								.build(request.gitUser())
				)
				.headers(setHeaders(request))
				.retrieve()
				.bodyToFlux(Repo.class)
				.filter(GetRepositories::isNotForkedRepository);
	}

	private static Consumer<HttpHeaders> setHeaders(final Request request) {
		return request.token().isEmpty()
				? HttpHeaders::clearContentHeaders
				: HttpHeaders -> HttpHeaders.setBearerAuth(request.token());
	}

	private static boolean isNotForkedRepository(final Repo repo) {
		return !repo.getFork();
	}

}
