package org.tinygame.herostory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @auther changmk
 * @date 2020/1/8 下午11:44
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        System.out.println("收到客户端消息=" + msg);

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        byte[] byteArray = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(byteArray);

        System.out.println("收到的字节为：");

        for (byte b : byteArray) {
            System.out.print(b);
            System.out.print(", ");
        }
        System.out.println();
    }
}
