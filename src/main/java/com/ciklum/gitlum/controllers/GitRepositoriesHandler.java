package com.ciklum.gitlum.controllers;

import com.ciklum.gitlum.domain.usecase.BuildGitRepositories;
import com.ciklum.gitlum.exception.ErrorContainer;
import com.ciklum.gitlum.exception.GithubUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
@RequiredArgsConstructor
@Slf4j
public class GitRepositoriesHandler {

	@Value("${constants.user-request-param-value}")
	private String userKey;
	@Value("${constants.page-request-param-value}")
	private String pageNumberKey;
	@Value("${constants.page-size-result-request-param-value}")
	private String resultsPerPageKey;
	@Value("${constants.default-page-number}")
	private String defaultPageNumber;
	@Value("${constants.default-results-per-page}")
	private String defaultResultsPerPage;

	private final BuildGitRepositories buildGitRepositories;

	public Mono<ServerResponse> getGitRepositories(final ServerRequest source) {
		final var request = buildRequest(source);
		log.info("Querying repositories and branches for user {}", request.gitUser());
		return buildGitRepositories
				.invoke(request)
				.collectList()
				.flatMap(product -> ok().bodyValue(product))
				.onErrorMap(
						WebClientResponseException.class,
						e -> new GithubUserNotFoundException(String.format("Github user %s not found", request.gitUser()))
				)
				.onErrorResume(
						GithubUserNotFoundException.class,
						e -> ok().bodyValue(new ErrorContainer(NOT_FOUND.value(), e.getMessage()))
				);
	}

	private Request buildRequest(final ServerRequest source) {
		final var gutUser = source.queryParam(userKey).get();
		final var pageNum = source.queryParam(pageNumberKey).orElse(defaultPageNumber);
		final var resultsPerPage = source.queryParam(resultsPerPageKey).orElse(defaultResultsPerPage);
		return new Request(gutUser, Integer.parseInt(pageNum), Integer.parseInt(resultsPerPage));
	}

}