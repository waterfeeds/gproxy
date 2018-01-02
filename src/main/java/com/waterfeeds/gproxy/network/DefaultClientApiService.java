package com.waterfeeds.gproxy.network;

import com.waterfeeds.gproxy.message.URI;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultClientApiService extends AbstractClientApiService {
    private ScheduledExecutorService executorService;
    private DefaultEventLoopGroup defaultEventLoopGroup;
    private NioEventLoopGroup nioEventLoopGroup;
    private Bootstrap bootstrap;
    private ChannelInitializer<SocketChannel> channelInitializer;
    private static int threads;

    static class StaticInitBean {
        public static DefaultClientApiService clientApiService = new DefaultClientApiService();
    }

    public static DefaultClientApiService newInstance(int threads) {
        DefaultClientApiService.threads = threads;
        return StaticInitBean.clientApiService;
    }

    private DefaultClientApiService() {
        resource();
        //start();
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                cleanUpClients();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    @Override
    public ChannelManager doConnect(URI uri) {
        String host = uri.getHost();
        int port = uri.getPort();
        try {
            start();
            ChannelFuture future = bootstrap.connect(host, port).sync();
            if (future.isSuccess()) {
                return new ChannelManager(true, future, new URI(host, port));
            }
        } catch (Exception e) {
            System.err.println("服务器连接失败");
            //log.error("服务器连接失败", e);
        }
        return null;
    }

    public final void cleanUpClients() {
        if (!channels.isEmpty()) {
            for (String key : channels.keySet()) {
                ChannelManager channelManager = channels.get(key);
                if (channelManager == null || !channelManager.isAvailable()) {
                    channels.remove(key);
                }
            }
        }
    }

    public void resource() {
        executorService = Executors.newScheduledThreadPool(2);
        this.defaultEventLoopGroup = new DefaultEventLoopGroup(threads, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "DEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
            }
        });

        this.nioEventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Client_" + index.incrementAndGet());
            }
        });
        this.bootstrap = new Bootstrap();
    }

    public Bootstrap start() {
        resource();
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                .option(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024)
                .handler(channelInitializer);
        return bootstrap;
    }


}
