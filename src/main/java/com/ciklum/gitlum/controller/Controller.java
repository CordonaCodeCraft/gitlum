package com.ciklum.gitlum.controller;


import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.usecase.BuildGitRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.ciklum.gitlum.config.Constants.CONTROLLER_BASE_URL;

@RestController
@RequestMapping(CONTROLLER_BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class Controller {

	private final BuildGitRepository buildGitRepository;

	@ApiOperation(
			value = "Serves a collection of Github repositories and their branches per the business requirements",
			tags = {"Get repositories and branches"}
	)
	@GetMapping(value = "/get")
	@ResponseStatus(HttpStatus.OK)
	public Flux<RepoDTO> getResult(@RequestBody final Request request) {
		log.info("Querying repositories and branches for user {}", request.gitUser());
		return buildGitRepository.invoke(request.gitUser());
	}

}
