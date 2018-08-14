package com.seantcanavan.backoff.exception;

public class UnexpectedExceptionException extends RuntimeException {

    private static final long serialVersionUID = 1893593817498282946L;

    public UnexpectedExceptionException(String message) {
        super(message);
    }
}
