package com.waterfeeds.gproxy.protocol.exception;

public class ProtocolException extends RuntimeException {
    private int errorCode;

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(int errorCode, String message) {
        super(errorCode + ": " + message);
    }
}
