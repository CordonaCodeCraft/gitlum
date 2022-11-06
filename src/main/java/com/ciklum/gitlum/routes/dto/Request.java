package com.ciklum.gitlum.routes.dto;

public record Request(String gitUser, Integer pageNumber, Integer resultsPerPage, String token) {}
