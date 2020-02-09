package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * @auther changmk
 * @date 2020/2/9 下午3:28
 */
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {

    /**
     * 处理信道消息
     * @param ctx
     * @param msg
     */
    void handle(ChannelHandlerContext ctx, TCmd msg);
}
