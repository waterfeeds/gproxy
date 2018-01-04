package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.proxy.base.NodeEventFilter;

public class DefaultFilter implements NodeEventFilter {

    @Override
    public boolean filter(String nodeName) {
        return true;
    }
}
