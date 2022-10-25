package com.ciklum.gitlum.routes.config;

import com.ciklum.gitlum.config.EndpointProperties;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.routes.filters.InvalidMediaTypeFilter;
import com.ciklum.gitlum.routes.filters.MissingUserParameterFilter;
import com.ciklum.gitlum.routes.handlers.GitRepositoriesHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RoutesConfig {

	private final EndpointProperties endpointProperties;

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
	public RouterFunction<ServerResponse> getGitRepositories(
			final GitRepositoriesHandler gitRepositoriesHandler,
			final InvalidMediaTypeFilter invalidMediaTypeFilter,
			final MissingUserParameterFilter missingUserParameterFilter
	) {
		return route(
				GET(endpointProperties.getBaseUrl() + endpointProperties.getGetRepositories()),
				gitRepositoriesHandler::getGitRepositories
		)
				.filter(invalidMediaTypeFilter)
				.filter(missingUserParameterFilter);
	}

}