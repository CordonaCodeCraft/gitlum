package com.ciklum.gitlum.controllers;

import com.ciklum.gitlum.domain.usecase.BuildGitRepositories;
import com.ciklum.gitlum.exception.ErrorContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
@RequiredArgsConstructor
@Slf4j
public class GitRepositoriesHandler {

	@Value("${constants.user-request-param-value}")
	private String user;
	@Value("${constants.page-request-param-value}")
	private String pageNumber;
	@Value("${constants.page-size-result-request-param-value}")
	private String resultsPerPage;
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
				.onErrorResume(
						WebClientResponseException.class,
						e -> ok().bodyValue(
								new ErrorContainer(HttpStatus.NOT_FOUND.value(), "Github user not found")
						)
				);
	}

	private Request buildRequest(final ServerRequest source) {
		final var gutUser = source.queryParam(user).get();
		final var pageNum = source.queryParam(pageNumber).orElse(defaultPageNumber);
		final var resultsPerPage = source.queryParam(this.resultsPerPage).orElse(defaultResultsPerPage);
		return new Request(gutUser, Integer.parseInt(pageNum), Integer.parseInt(resultsPerPage));
	}

}