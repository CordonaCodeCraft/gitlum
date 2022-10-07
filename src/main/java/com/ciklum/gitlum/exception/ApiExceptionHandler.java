package com.ciklum.gitlum.exception;

import com.ciklum.gitlum.exception.containers.GitUserNotFoundExceptionContainer;
import com.ciklum.gitlum.exception.containers.MessageNotAcceptableExceptionContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

	@ExceptionHandler(value = {WebClientResponseException.class})
	public ResponseEntity<Object> handleGitUserNotFoundException(final WebClientResponseException exception) {
		final var notFound = NOT_FOUND;
		final var message = buildErrorMessage(requireNonNull(exception.getMessage()));
		final var container = new GitUserNotFoundExceptionContainer(notFound.value(), message);
		log.error(message);
		return new ResponseEntity<>(container, notFound);
	}

	@ExceptionHandler(value = {HttpMessageNotReadableException.class})
	public ResponseEntity<Object> handleMessageNotAcceptableException() {
		final var notAcceptable = NOT_ACCEPTABLE;
		final var message = "Invalid content type (XML) provided. JSON content type required";
		final var container = new MessageNotAcceptableExceptionContainer(notAcceptable.value(), message);
		log.error(message);
		return new ResponseEntity<>(container, notAcceptable);
	}

	private String buildErrorMessage(final String message) {
		final var s = message.replace("/repos", "");
		return String.format("Github user with username %s not found", s.substring(s.lastIndexOf("/") + 1));
	}

}
