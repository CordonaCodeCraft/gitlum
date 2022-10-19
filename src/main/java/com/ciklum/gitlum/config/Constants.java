package com.ciklum.gitlum.config;

import org.springframework.http.HttpStatus;

public class Constants {
	public static final String CONTROLLER_BASE_URL = "api/v1/git-repositories";
	public static final String ACCEPTABLE_REQUEST_FORMAT = "JSON";
	public static final String NON_ACCEPTABLE_REQUEST_FORMAT = "XML";
	public static final String NOT_ACCEPTABLE_REQUEST_ERROR_MESSAGE = "Invalid content type (%S) provided. %s content type required";
	public static final String USER_NOT_FOUND_ERROR_MESSAGE = "Github user with username %s not found";
	public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
	public static final HttpStatus NOT_ACCEPTABLE = HttpStatus.NOT_ACCEPTABLE;

}
