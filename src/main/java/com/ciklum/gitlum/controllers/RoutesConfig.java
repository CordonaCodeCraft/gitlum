package com.ciklum.gitlum.controllers;

import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RoutesConfig {

	@Value("${constants.git-repositories-base-url}")
	private String baseURL;
	@Value("${constants.get-git-repositories-uri}")
	private String getGitRepositoriesURI;

	@Bean
	@RouterOperation(
			method = GET,
			path = "/api/v1/git-repositories/get",
			produces = {APPLICATION_JSON_VALUE},
			beanClass = GitRepositoriesHandler.class,
			beanMethod = "getGitRepositories",
			operation = @Operation(
					operationId = "getGitRepositories",
					responses = {
							@ApiResponse(
									responseCode = "200",
									description = "Successful operation",
									content = @Content(schema = @Schema(implementation = RepoDTO.class))
							)
					}
			)
	)
	public RouterFunction<ServerResponse> getGitRepositories(final GitRepositoriesHandler gitRepositoriesHandler) {
		final RequestPredicate uri = GET(baseURL + getGitRepositoriesURI);
		final RequestPredicate mediaType = accept(APPLICATION_JSON);
		return route(uri.and(mediaType), gitRepositoriesHandler::getGitRepositories);
	}


	@Bean
	public RouterFunction<ServerResponse> handleInvalidMediaType(final InvalidMediaTypeErrorHandler invalidMediaTypeErrorHandler) {
		final RequestPredicate uri = GET(baseURL + getGitRepositoriesURI);
		final RequestPredicate mediaType = accept(APPLICATION_XML);
		return route(uri.and(mediaType), invalidMediaTypeErrorHandler::handleInvalidMediaTypeError);
	}

}