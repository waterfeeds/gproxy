package com.waterfeeds.gproxy.proxy.channel;

import com.waterfeeds.gproxy.network.ChannelManager;
import com.waterfeeds.gproxy.session.Context;
import com.waterfeeds.gproxy.session.Session;
import io.netty.channel.Channel;

public class ClientChannel {
    private ChannelManager manager;
    private Context context;
    private Session session;

    public ClientChannel(ChannelManager manager) {
        this.manager = manager;
    }

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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isAvailable() {
        return manager.isAvailable();
    }

    public Channel getChannel() {
        return manager.getChannel();
    }
}
