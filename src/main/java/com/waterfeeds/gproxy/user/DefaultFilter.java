package com.waterfeeds.gproxy.user;

import com.waterfeeds.gproxy.proxy.base.NodeEventFilter;

public class DefaultFilter implements NodeEventFilter {
    private DefaultFilter() {

    }

    private static class ClassHolder {
        private static DefaultFilter instance = new DefaultFilter();
    }

    public static NodeEventFilter getFilter() {
        return ClassHolder.instance;
    }

    @Override
    public boolean filter(String nodeName) {
        return true;
    }
}
