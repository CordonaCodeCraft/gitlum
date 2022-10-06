package com.ciklum.gitlum.controller;


import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.usecase.ProduceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ciklum.gitlum.config.Constants.CONTROLLER_BASE_URL;

@RestController
@RequestMapping(CONTROLLER_BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class Controller {

	private final ProduceResult produceResult;

	@GetMapping("/get")
	public List<RepoDTO> getResult(@RequestBody final Request request) {
		log.info("Produced result");
		return produceResult.invoke(request.gitUser());
	}

}
