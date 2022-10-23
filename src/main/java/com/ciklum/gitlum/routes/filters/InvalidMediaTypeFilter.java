package com.ciklum.gitlum.routes.filters;

import com.ciklum.gitlum.exception.ErrorContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@Slf4j
public class InvalidMediaTypeFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

	@Override
	public Mono<ServerResponse> filter(final ServerRequest request, final HandlerFunction<ServerResponse> response) {

		if (request.headers().accept().contains(APPLICATION_XML)) {
			final var errorMessage = String.format("Invalid media type: %s", APPLICATION_XML);
			log.info(errorMessage);
			return ok().bodyValue(new ErrorContainer(406, errorMessage));
		}
		return response.handle(request);
	}

}