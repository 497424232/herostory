package org.tinygame.herostory;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息解码器
 *
 * Created by changmk on 2020/1/15
 * @author Administrator
 * */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GameMsgDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        // websocket 二进制消息会通过HttpServerCodec 解码成 BinaryWebSocketFrame 类对象
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();


        // 消息前两位字节表示消息长度，三四位字节表示消息编号，之后的表示消息体
        // 读取消息长度
        byteBuf.readShort();
        // 读取消息编号
        int msgCode = byteBuf.readShort();

        // 获取消息构建者
        Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
        if (null == msgBuilder) {
            logger.error("无法识别消息，msgCode={}", msgCode);
            return;
        }

        // 拿到消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        msgBuilder.clear();
        msgBuilder.mergeFrom(msgBody);
        Message newMsg = msgBuilder.build();

        if (null != newMsg) {
            ctx.fireChannelRead(newMsg);
        }
    }
}
