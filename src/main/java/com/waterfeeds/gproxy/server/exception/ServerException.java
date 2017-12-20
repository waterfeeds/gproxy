package com.waterfeeds.gproxy.server.exception;

public class ServerException extends RuntimeException {
    private int errorCode;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(int errorCode, String message) {
        super(errorCode + ": " + message);
    }
}
