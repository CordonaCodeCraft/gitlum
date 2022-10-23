package com.ciklum.gitlum.controllers;

public record Request(
		String gitUser,
		Integer pageNumber,
		Integer resultsPerPage,
		String token
) {
}
