package com.waterfeeds.gproxy.protocol.base;

public class GproxyErrorCode {
    public static int BAD_PROTOCOL_CODE = 10001;
    public static String BAD_PROTOCOL_MESSAGE = "协议格式不正确";

    public static int UNSAFE_PROTOCOL_CODE = 10002;
    public static String UNSAFE_PROTOCOL_MESSAGE = "协议验签失败";
}
