package com.ciklum.gitlum.controllers;

import com.ciklum.gitlum.exception.ErrorContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@Slf4j
public class InvalidMediaTypeErrorHandler {

	public Mono<ServerResponse> handleInvalidMediaTypeError(final ServerRequest source) {
		final var errorMessage = String.format("Invalid media type: %s", APPLICATION_XML);
		log.info(errorMessage);
		return ok().bodyValue(new ErrorContainer(406, errorMessage));
	}

}