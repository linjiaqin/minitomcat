package com.ljq.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {
    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private int port;
    public NettyServer(int port){
        this.port = port;
    }
    public void start()  {
        logger.info("netty server is starting ...");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new HttpHandle());
                    }
                })
                //option()用来配置NioServerSocketChannel(负责接收到来的connection)，
                .option(ChannelOption.SO_BACKLOG, 128)
                //childOption()是用来配置被ServerChannel(这里是NioServerSocketChannel) 所接收的Channel
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        //当令Channel开始一个I/O操作时,会创建一个新的ChannelFuture去异步完成操作.
        ChannelFuture f = null;
        try {
            f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
