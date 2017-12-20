package com.waterfeeds.gproxy.protocol;

public class GproxyBody {
    private String safeSign;
    private String content;

    public GproxyBody(String content) {
        this.safeSign = "";
    }

    public GproxyBody(String safeSign, String content) {
        this.safeSign = safeSign;
        this.content = content;
    }

    public int getContentLen() {
        return safeSign.length() + content.length();
    }

    public String getSafeSign() {
        return safeSign;
    }

    public void setSafeSign(String safeSign) {
        this.safeSign = safeSign;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
