package com.waterfeeds.gproxy.proxy.filter;

import com.waterfeeds.gproxy.proxy.base.NodeEventFilter;

public class Filter2 implements NodeEventFilter {

    @Override
    public boolean filter(String serverName) {
        if (serverName.contains("server-02")) {
            return true;
        }
        return false;
    }
}
