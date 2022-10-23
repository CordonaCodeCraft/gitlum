package com.ciklum.gitlum.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class RequestLimitExceededException extends WebClientException {

	public RequestLimitExceededException(final String errorMessage) {
		super(errorMessage);
	}
}