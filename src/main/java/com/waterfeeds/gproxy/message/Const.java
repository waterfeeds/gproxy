package com.waterfeeds.gproxy.message;

public class Const {
    public static String SUCCESS_CODE = "R200";
    public static String ERROR_CODE = "R300";
    public static long TIME_OUT = 3000;
    public static int ZOOKEEPER_CONNECTION_TIME_OUT = 5000;

    public static int HEAD_DATA = 0x76;

    public static String SAFE_SIGN = "PvAu9PcgpVCzjBG4";

    public static String ZOOKEEPER_NAMESPACE_SERVERS = "servers";

    public static String CLIENT_ID = "cid";
    public static String USER_ID = "uid";
    public static String GROUP_ID = "gid";
    public static String MESSAGE = "msg";

    public static int ROUTER_RANDOM = 1;
    public static int ROUTER_ASSIGN = 2;
}
