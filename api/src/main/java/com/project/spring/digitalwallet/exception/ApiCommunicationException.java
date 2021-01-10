package com.project.spring.digitalwallet.exception;

public class ApiCommunicationException extends RuntimeException {

    public ApiCommunicationException() {
        super();
    }

    public ApiCommunicationException(String s) {
        super(s);
    }

    public ApiCommunicationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApiCommunicationException(Throwable throwable) {
        super(throwable);
    }

    protected ApiCommunicationException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
