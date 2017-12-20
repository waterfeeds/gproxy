package com.waterfeeds.gproxy.protocol;

public class GproxyHeader {
    private int cmd;
    private boolean safe;
    private int contentLen;

    public GproxyHeader(int cmd, int contentLen) {
        this.cmd = cmd;
        this.safe = false;
        this.contentLen = contentLen;
    }
    public GproxyHeader(int cmd, boolean safe, int contentLen) {
        this.cmd = cmd;
        this.safe = safe;
        this.contentLen = contentLen;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public int getContentLen() {
        return contentLen;
    }

    public void setContentLen(int contentLen) {
        this.contentLen = contentLen;
    }
}
