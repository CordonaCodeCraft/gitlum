package com.ciklum.gitlum.exception.containers;

public record GitUserNotFoundExceptionContainer(int httpStatus, String message) {
}
