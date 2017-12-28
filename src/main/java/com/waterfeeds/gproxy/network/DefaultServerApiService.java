package com.waterfeeds.gproxy.network;

import com.alibaba.dubbo.rpc.Invocation;
import com.waterfeeds.gproxy.proxy.Proxy;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultServerApiService extends ServerApiService implements InitializingBean {
    private ChannelInitializer<SocketChannel> channelInitializer;
    private DefaultEventLoopGroup serverLoopGroup;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workGroup;
    private ServerBootstrap b;
    private int port;
    private ChannelFuture sync;
    private final int TOP_LENGTH = 129 >> 1 | 34;
    private final int TOP_HEARTBEAT = 129 >> 1 | 36;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChannelInitializer<SocketChannel> getChannelInitializer() {
        return channelInitializer;
    }

    public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public void resource() {
        serverLoopGroup = new DefaultEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "DEFAULTEVENTLOOPGROUP_" + index.incrementAndGet());
            }
        });
        bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });

        workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "WORK_" + index.incrementAndGet());
            }
        });

        b = new ServerBootstrap();
    }

    public boolean start() {
        resource();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                .option(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(channelInitializer);
        try {
            sync = b.bind(port).sync();
            System.out.println(port + " 启动成功");
            log.info(this.getClass().getName() + "启动成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(this.getClass().getName() + "启动失败", e);
            return false;
        }
    }

    @Override
    public Runnable getSubmitTask(final Channel channel, final Invocation invocation) {
        return new Runnable() {
            @Override
            public void run() {
                if (invocation != null) {
                    doWork(channel, invocation);
                } else {
                    log.info("server 超过负载 丢弃任务");
                }
            }

            private void doWork(final Channel channel, final Invocation invocation) {
                log.info("do work");
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }
}
