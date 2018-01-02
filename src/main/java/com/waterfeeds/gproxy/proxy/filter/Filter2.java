package com.waterfeeds.gproxy.proxy.filter;

import com.waterfeeds.gproxy.proxy.base.NodeEventFilter;

public class Filter2 implements NodeEventFilter {
    private Filter2() {

    }

    private static class ClassHolder {
        private static Filter2 instance = new Filter2();
    }

    public static NodeEventFilter getFilter() {
        return ClassHolder.instance;
    }

    @Override
    public boolean filter(String serverName) {
        if (serverName.contains("server-02")) {
            return true;
        }
        return false;
    }
}
