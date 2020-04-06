package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 主线程处理器
 */
public final class MainThreadProcessor {

    private final Logger logger = LoggerFactory.getLogger(MainThreadProcessor.class);

    /**
     * 单例对象
     */
    static private final MainThreadProcessor instance = new MainThreadProcessor();

    /**
     * 创建单线程线程池
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread newThread = new Thread(r);
            newThread.setName("mainThreadProcessor");
            return newThread;
        }
    });

    private MainThreadProcessor() {

    }

    public static MainThreadProcessor getInstance() {
        return instance;
    }

    /**
     * 处理消息
     *
     * @param ctx
     * @param msg
     */
    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg) {

        if (null == ctx || null == msg) {
            return;
        }

        this.executorService.submit(()->{
            //获取消息类信息
            Class msgClazz = msg.getClass();
            ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msgClazz);

            if (null == cmdHandler) {
                logger.error("未找到相对应的指令处理器，msgClazz={}", msgClazz);
            }

            //todo 不要忘了用 try … catch … 来裹住代码，避免出现异常导致线程终止；
            try {
                cmdHandler.handle(ctx, cast(msg));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        });
    }


    /**
     * 转型消息对象--转换msg
     *
     * @param msg
     * @param <TCmd>
     * @return
     */
    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if (null == msg) {
            return null;
        } else {
            return (TCmd) msg;
        }
    }


}
