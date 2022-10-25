package com.ciklum.gitlum.routes.filters;

import com.ciklum.gitlum.config.RequestProperties;
import com.ciklum.gitlum.exception.ErrorContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
@Slf4j
public class InvalidMediaTypeFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

	private final RequestProperties requestProperties;

	@Override
	public Mono<ServerResponse> filter(final ServerRequest request, final HandlerFunction<ServerResponse> response) {
		final var invalidMediaType = findInvalidMediaTypeIn(request);
		if (invalidMediaType.isPresent()) {
			final var errorMessage = String.format("Invalid media type: %s", invalidMediaType.get());
			log.info(errorMessage);
			return ok().bodyValue(new ErrorContainer(406, errorMessage));
		}
		return response.handle(request);
	}

	private Optional<MediaType> findInvalidMediaTypeIn(final ServerRequest request) {
		return request
				.headers()
				.accept()
				.stream()
				.filter(mediaType -> !requestProperties.getValidMediaTypes().contains(mediaType.toString()))
				.findFirst();
	}

}