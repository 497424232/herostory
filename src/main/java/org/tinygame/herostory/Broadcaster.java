package org.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 消息广播员
 * @auther changmk
 * @date 2020/2/9 下午3:04
 */
public final class Broadcaster {

    /**
     * 客户端信道数组，必须用static
     */
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster(){

    }

    /**
     * 添加信道
     * @param channel
     */
    public static void addChannel(Channel channel) {
        channelGroup.add(channel);
    }

    /**
     * 移除信道
     * @param channel
     */
    public static void removeChannel(Channel channel){
        channelGroup.remove(channel);
    }

    /**
     * 广播消息
     * @param msg
     */
    public static void broadcast(Object msg) {
        if (null != msg) {
            return;
        }
        channelGroup.writeAndFlush(msg);
    }
}
