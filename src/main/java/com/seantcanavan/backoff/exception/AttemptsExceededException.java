package com.seantcanavan.backoff.exception;

public class AttemptsExceededException extends RuntimeException {

    private static final long serialVersionUID = -6489528567239637001L;

    public AttemptsExceededException(String message) {
        super(message);
    }
}
