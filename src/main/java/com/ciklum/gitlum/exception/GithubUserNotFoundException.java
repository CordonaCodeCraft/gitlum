package com.ciklum.gitlum.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class GithubUserNotFoundException extends WebClientException {

	public GithubUserNotFoundException(String message) {
		super(message);
	}
}