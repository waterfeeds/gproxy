package com.waterfeeds.gproxy.protocol.exception;

import com.alibaba.fastjson.JSONObject;

public class ProtocolException extends RuntimeException {
    private int errorCode;
    private String errorMessage;

    public ProtocolException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public ProtocolException(int errorCode, String errorMessage) {
        super(errorCode + ": " + errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        JSONObject object = new JSONObject();
        object.put("err_code", errorCode);
        object.put("err_message", errorMessage);
        return object.toString();
    }
}
