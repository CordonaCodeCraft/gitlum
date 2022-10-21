package com.ciklum.gitlum.exception;

public record ErrorContainer(int httpStatus, String message) {
}