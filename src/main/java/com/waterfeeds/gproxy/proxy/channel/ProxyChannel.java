package com.waterfeeds.gproxy.proxy.channel;

import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.session.Context;

public class ProxyChannel {
    private ChannelManager manager;
    private Context context;

    public ChannelManager getManager() {
        return manager;
    }

    public void setManager(ChannelManager manager) {
        this.manager = manager;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
