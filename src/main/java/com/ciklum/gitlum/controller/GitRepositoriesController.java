package com.ciklum.gitlum.controller;


import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.usecase.BuildGitRepositories;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.ciklum.gitlum.config.Constants.CONTROLLER_BASE_URL;

@RestController
@RequestMapping(CONTROLLER_BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class GitRepositoriesController {

	private final BuildGitRepositories buildGitRepositories;

	@ApiOperation(
			value = "Serves a collection of Github repositories and their branches per the business requirements",
			tags = {"Get repositories and branches"}
	)
	@GetMapping(value = "/get")
	@ResponseStatus(HttpStatus.OK)
	public Flux<RepoDTO> buildGitRepositories(@RequestParam(name = "user") String gitUser,
											  @RequestParam(name = "page", defaultValue = "1") int pageNumber,
											  @RequestParam(name = "per_page", defaultValue = "30") int resultsPerPage) {
		log.info("Querying repositories and branches for user {} at {} page with {} results per page", gitUser, pageNumber, resultsPerPage);
		return buildGitRepositories.invoke(new Request(gitUser, pageNumber, resultsPerPage));
	}

}
