package com.waterfeeds.gproxy.protocol.base;

public class GproxyCommand {
    public static final int SEND_TO_ALL = 1;
    public static final int SEND_TO_CLIENT = 2;
    public static final int SEND_TO_USER = 3;
    public static final int SEND_TO_GROUP = 4;
    public static final int GET_CLIENT_COUNT = 5;
    public static final int GET_USER_COUNT = 6;
    public static final int GET_GROUP_COUNT = 7;
    public static final int BIND_UID = 8;
    public static final int UN_BIND_UID = 9;
    public static final int JOIN_GROUP = 10;
    public static final int LEAVE_GROUP = 11;

    public static final int CLIENT_EVENT = 101;
    public static final int SERVER_EVENT = 102;
}
