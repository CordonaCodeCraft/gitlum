package com.ciklum.gitlum.exception;

import org.springframework.web.reactive.function.client.WebClientException;

public class WrongCredentialsException extends WebClientException {

  public WrongCredentialsException(final String errorMessage) {
    super(errorMessage);
  }
}
