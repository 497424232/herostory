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
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;


/**
 * 服务启动类
 *
 * @auther changmk
 * @date 2020/1/8 下午11:09
 */
public class ServerMain {


//    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {

        // 注册所有handler
        CmdHandlerFactory.init();
        // 初始化消息转换map
        GameMsgRecognizer.init();

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
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),
                        new GameMsgDecoder(),
                        new GameMsgEncoder(),
                        new GameMsgHandler()
                );
            }
        });
        try {
            // 绑定 12345 端口,
            // 注意: 实际项目中会使用 argArray 中的参数来指定端口号
            ChannelFuture f = serverBootstrap.bind(12345).sync();

            if (f.isSuccess()) {
                System.out.println("启动服务成功");
//                logger.info("服务器启动成功!");
            }

            // 等待服务器信道关闭,
            // 也就是不要立即退出应用程序, 让应用程序可以一直提供服务
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
