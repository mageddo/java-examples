package com.example;

public class ApiClientException extends RuntimeException {

    private final int status;

    public ApiClientException(int status, String body) {
        super("HTTP %d: %s".formatted(status, body));
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
