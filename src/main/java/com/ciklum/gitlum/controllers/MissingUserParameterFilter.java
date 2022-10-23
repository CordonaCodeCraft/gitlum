package com.ciklum.gitlum.controllers;

import com.ciklum.gitlum.exception.ErrorContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@Slf4j
public class MissingUserParameterFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

	@Value("${constants.user-request-param-value}")
	private String userKey;

	@Override
	public Mono<ServerResponse> filter(final ServerRequest request, final HandlerFunction<ServerResponse> response) {

		if (request.queryParam(userKey).isEmpty()) {
			final var errorMessage = "Github user not provided";
			log.info(errorMessage);
			return ok().bodyValue(new ErrorContainer(BAD_REQUEST.value(), errorMessage));
		}
		return response.handle(request);
	}

}