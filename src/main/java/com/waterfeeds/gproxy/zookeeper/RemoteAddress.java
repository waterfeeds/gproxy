package com.waterfeeds.gproxy.zookeeper;

import com.waterfeeds.gproxy.common.message.URI;

import java.io.Serializable;

public class RemoteAddress implements Serializable {
    private static final long serialVersionUID = 122L;
    private String nodeName;
    private URI uri;

    public RemoteAddress() {

    }
    public RemoteAddress(String nodeName, URI uri) {
        this.nodeName = nodeName;
        this.uri = uri;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
