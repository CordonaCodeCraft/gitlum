package com.ciklum.gitlum.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;




@Configuration
public class RoutesConfig {

	@Value("${constants.git-repositories-base-url}")
	private String baseURL;
	@Value("${constants.get-git-repositories-uri}")
	private String getGitRepositoriesURI;

	@Bean
	public RouterFunction<ServerResponse> getGitRepositories(final GitRepositoriesHandler gitRepositoriesHandler) {
		return route()
				.GET(baseURL + getGitRepositoriesURI, gitRepositoriesHandler::getGitRepositories)
				.build();
	}

}