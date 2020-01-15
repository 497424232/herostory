package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.sun.javafx.logging.JFRInputEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 消息解码器
 *
 * Created by changmk on 2020/1/15
 * @author Administrator
 * */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

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

        byte[] msgBytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBytes);

        GeneratedMessageV3 cmd = null;

        switch (msgCode) {
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE :
                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgBytes);
            break;

            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE :
                cmd = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBytes);
            break;

            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE :
                cmd = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBytes);
            break;

        }

        if (null != cmd) {
            ctx.fireChannelRead(cmd);
        }
    }
}
