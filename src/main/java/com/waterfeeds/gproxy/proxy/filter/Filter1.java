package com.waterfeeds.gproxy.proxy.filter;

import com.waterfeeds.gproxy.proxy.base.NodeEventFilter;

public class Filter1 implements NodeEventFilter {

    @Override
    public boolean filter(String serverName) {
        if (serverName.contains("server-01") || serverName.contains("server-03")) {
            return true;
        }
        return false;
    }
}
