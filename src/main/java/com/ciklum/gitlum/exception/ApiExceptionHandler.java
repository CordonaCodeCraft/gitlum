package com.ciklum.gitlum.exception;

import com.ciklum.gitlum.exception.containers.GitUserNotFoundExceptionContainer;
import com.ciklum.gitlum.exception.containers.RequestNotAcceptableExceptionContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static com.ciklum.gitlum.config.Constants.ACCEPTABLE_REQUEST_FORMAT;
import static com.ciklum.gitlum.config.Constants.NON_ACCEPTABLE_REQUEST_FORMAT;
import static com.ciklum.gitlum.config.Constants.NOT_ACCEPTABLE;
import static com.ciklum.gitlum.config.Constants.NOT_ACCEPTABLE_REQUEST_ERROR_MESSAGE;
import static com.ciklum.gitlum.config.Constants.NOT_FOUND;
import static com.ciklum.gitlum.config.Constants.USER_NOT_FOUND_ERROR_MESSAGE;
import static java.util.Objects.requireNonNull;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(value = {WebClientResponseException.class})
	public ResponseEntity<Object> handleGitUserNotFoundException(final WebClientResponseException exception) {
		final var errorMessage = buildErrorMessage(requireNonNull(exception.getMessage()));
		final var container = new GitUserNotFoundExceptionContainer(NOT_FOUND.value(), errorMessage);
		log.error(errorMessage);
		return new ResponseEntity<>(container, NOT_FOUND);
	}

	@ExceptionHandler(value = {HttpMessageNotReadableException.class})
	public ResponseEntity<Object> handleRequestNotAcceptableException() {
		final var errorMessage =
				String.format(
						NOT_ACCEPTABLE_REQUEST_ERROR_MESSAGE,
						NON_ACCEPTABLE_REQUEST_FORMAT,
						ACCEPTABLE_REQUEST_FORMAT
				);
		final var container = new RequestNotAcceptableExceptionContainer(NOT_ACCEPTABLE.value(), errorMessage);
		log.error(errorMessage);
		return new ResponseEntity<>(container, NOT_ACCEPTABLE);
	}

	private String buildErrorMessage(final String message) {
		return String.format(USER_NOT_FOUND_ERROR_MESSAGE, extractUserName(message));
	}

	private String extractUserName(final String source) {
		final var s = source.replace("/repos", "");
		return s.substring(s.lastIndexOf("/") + 1);
	}

}
