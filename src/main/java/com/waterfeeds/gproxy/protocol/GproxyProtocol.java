package com.waterfeeds.gproxy.protocol;

public class GproxyProtocol {
    private GproxyHeader header;
    private GproxyBody body;

    public GproxyProtocol(GproxyHeader header, GproxyBody body) {
        this.header = header;
        this.body = body;
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
}
