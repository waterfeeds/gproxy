package com.waterfeeds.gproxy.protocol;

import com.waterfeeds.gproxy.message.Const;

public class GproxyProtocol {
    private int identifier = Const.HEAD_DATA;
    private GproxyHeader header;
    private GproxyBody body;

    public GproxyProtocol(GproxyHeader header, GproxyBody body) {
        this.header = header;
        this.body = body;
    }

    public int getIdentifier() {
        return identifier;
    }

    public GproxyHeader getHeader() {
        return header;
    }

    public void setHeader(GproxyHeader header) {
        this.header = header;
    }

    public GproxyBody getBody() {
        return body;
    }

    public void setBody(GproxyBody body) {
        this.body = body;
    }

    public int getLength() {
        if (getHeader().getSafe() == 0) {
            return header.getContentLen() + 4;
        }
        else {
            return header.getContentLen() + 20;
        }
    }

    public boolean isSafe() {
        if (header.getSafe() == 1 && !body.getSafeSign().equals(Const.SAFE_SIGN)) {
            return false;
        }
        return true;
    }
}
