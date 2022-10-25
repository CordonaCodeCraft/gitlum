package com.ciklum.gitlum.routes.handlers;

import com.ciklum.gitlum.config.RequestProperties;
import com.ciklum.gitlum.domain.usecase.BuildGitRepositories;
import com.ciklum.gitlum.exception.ErrorContainer;
import com.ciklum.gitlum.exception.GithubUserNotFoundException;
import com.ciklum.gitlum.exception.RequestLimitExceededException;
import com.ciklum.gitlum.exception.WrongCredentialsException;
import com.ciklum.gitlum.routes.dto.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.lang.Integer.parseInt;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Component
@RequiredArgsConstructor
@Slf4j
public class GitRepositoriesHandler {

	private static final String EMPTY = "";

	private final RequestProperties requestProperties;
	private final BuildGitRepositories buildGitRepositories;

	public Mono<ServerResponse> getGitRepositories(final ServerRequest source) {
		final var request = buildRequest(source);
		log.info("Querying repositories and branches for user {}", request.gitUser());
		return buildGitRepositories
				.invoke(request)
				.collectList()
				.flatMap(product -> ok().bodyValue(product))
				.onErrorMap(
						WebClientResponseException.NotFound.class,
						e -> new GithubUserNotFoundException(String.format("Github user %s not found", request.gitUser()))
				)
				.onErrorMap(
						WebClientResponseException.Forbidden.class,
						e -> new RequestLimitExceededException("Request limit exceeded"))
				.onErrorMap(
						WebClientResponseException.Unauthorized.class,
						e -> new WrongCredentialsException("Wrong credentials")
				)
				.onErrorResume(
						GithubUserNotFoundException.class,
						e -> ok().bodyValue(new ErrorContainer(NOT_FOUND.value(), e.getMessage()))
				)
				.onErrorResume(
						RequestLimitExceededException.class,
						e -> ok().bodyValue(new ErrorContainer(FORBIDDEN.value(), e.getMessage()))
				)
				.onErrorResume(
						WrongCredentialsException.class,
						e -> ok().bodyValue(new ErrorContainer(UNAUTHORIZED.value(), e.getMessage()))
				);
	}

	private Request buildRequest(final ServerRequest source) {
		final var gutUser = source.queryParam(requestProperties.getUserParamKey()).get();
		final var pageNum = source.queryParam(requestProperties.getPageNumberParamKey()).orElse(requestProperties.getDefaultPageNumber());
		final var resultsPerPage = source.queryParam(requestProperties.getResultsPerPageParamValue()).orElse(requestProperties.getDefaultResultsPerPage());
		final var token = source
				.headers()
				.header(requestProperties.getAuthorizationHeaderKey())
				.stream()
				.filter(e -> e.contains(requestProperties.getTokenPrefix()))
				.map(s -> s.replace(requestProperties.getTokenPrefix(), EMPTY))
				.findFirst()
				.orElse(EMPTY);
		return new Request(gutUser, parseInt(pageNum), parseInt(resultsPerPage), token);
	}

}