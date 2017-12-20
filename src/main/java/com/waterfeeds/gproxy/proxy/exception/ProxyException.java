package com.waterfeeds.gproxy.proxy.exception;

public class ProxyException extends RuntimeException {
    private int errorCode;

    public ProxyException(String message) {
        super(message);
    }

    public ProxyException(int errorCode, String message) {
        super(errorCode + ": " + message);
    }
}

