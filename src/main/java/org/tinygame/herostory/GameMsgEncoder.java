package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 消息编码器
 *
 * Created by changmk on 2020/1/15
 **/
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (null == msg || !(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }

        // 获取消息编码
        int msgCode = GameMsgRecognizer.getMsgCodeByMsgClazz(msg.getClass());
        if (msgCode <= -1) {
            logger.error("消息类型无法识别，msgClazz={}", msg.getClass().getName());
            return;
        }

        byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();

        ByteBuf byteBuf = ctx.alloc().buffer();
        // 写消息0，目前为了占位
        byteBuf.writeShort((short)0);
        // 写消息编号
        byteBuf.writeShort((short) msgCode);
        // 写消息体
        byteBuf.writeBytes(msgBody);

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        super.write(ctx, frame, promise);
    }
}
