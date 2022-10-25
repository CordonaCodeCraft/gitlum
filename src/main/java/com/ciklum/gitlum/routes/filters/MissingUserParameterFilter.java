package com.ciklum.gitlum.routes.filters;

import com.ciklum.gitlum.config.RequestProperties;
import com.ciklum.gitlum.exception.ErrorContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
@Slf4j
public class MissingUserParameterFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

	private final RequestProperties requestProperties;

	@Override
	public Mono<ServerResponse> filter(final ServerRequest request, final HandlerFunction<ServerResponse> response) {
		if (request.queryParam(requestProperties.getUserParamKey()).isEmpty()) {
			final var errorMessage = "Github user not provided";
			log.info(errorMessage);
			return ok().bodyValue(new ErrorContainer(BAD_REQUEST.value(), errorMessage));
		}
		return response.handle(request);
	}

}