package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @auther changmk
 * @date 2020/1/8 下午11:09
 */
public class ServerMain {


    private final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {

        //接收任务的事件
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //处理业务的事件组
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new HttpServerCodec(),
                        new HttpObjectAggregator(65533),
                        new WebSocketServerProtocolHandler("/websocket"),
                        new GameMsgHandler()
                );
            }
        });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(12345).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("启动服务成功");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
