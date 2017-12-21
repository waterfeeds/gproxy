package com.waterfeeds.gproxy.protocol;

public class GproxyHeader {
    private int cmd;
    private int safe;
    private int contentLen;

    public GproxyHeader(int cmd, int contentLen) {
        this.cmd = cmd;
        this.safe = 0;
        this.contentLen = contentLen;
    }

    public GproxyHeader(int cmd, int safe, int contentLen) {
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

    public int getSafe() {
        return safe;
    }

    public void setSafe(int safe) {
        this.safe = safe;
    }

    public int getContentLen() {
        return contentLen;
    }

    public void setContentLen(int contentLen) {
        this.contentLen = contentLen;
    }
}
