package com.waterfeeds.gproxy.network;

import com.waterfeeds.gproxy.message.Const;
import com.waterfeeds.gproxy.message.URI;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultNettyClientApiService extends NettyClientApiService {
    private ScheduledExecutorService executorService;
    private DefaultEventLoopGroup defaultEventLoopGroup;
    private NioEventLoopGroup nioEventLoopGroup;
    private Bootstrap bootstrap;
    private static int threads;

    static class staticInitBean {
        public static DefaultNettyClientApiService clientApiService = new DefaultNettyClientApiService();
    }

    public static DefaultNettyClientApiService newInstance(int threads) {
        DefaultNettyClientApiService.threads = threads;
        return staticInitBean.clientApiService;
    }

    private DefaultNettyClientApiService() {
        resource();
        start();
        executorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                cleanUpClients();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public ChannelManager doConnect(URI uri) {
        try {
            ChannelManager channelManager = null;

        } catch (Exception e) {
            log.error(this.getClass().getName(), e);
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

            public Thread newThread(Runnable r) {
                return new Thread(r, "DEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
            }
        });

        this.nioEventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "Client_" + index.incrementAndGet());
            }
        });
        this.bootstrap = new Bootstrap();
    }

    public Bootstrap start() {
        bootstrap.group(defaultEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                .option(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defaultEventLoopGroup);
                        //ch.pipeline().addLast(new ClientEncode());
                        //ch.pipeline().addLast(new ClinetDecode());
                        ch.pipeline().addLast(new IdleStateHandler(20, 0, 0));
                        //ch.pipeline().addLast(new OutChannelInvocationHandler());

                    }
                });
        return bootstrap;
    }


}
