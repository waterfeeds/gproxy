package com.waterfeeds.gproxy.proxy.filter;

import com.waterfeeds.gproxy.proxy.base.NodeEventFilter;

public class Filter1 implements NodeEventFilter {
    private Filter1() {

    }

    private static class ClassHolder {
        private static Filter1 instance = new Filter1();
    }

    public static NodeEventFilter getFilter() {
        return ClassHolder.instance;
    }

    @Override
    public boolean filter(String serverName) {
        if (serverName.contains("server-01") || serverName.contains("server-03")) {
            return true;
        }
        return false;
    }
}
