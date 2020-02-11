package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令处理工厂
 * @auther changmk
 * @date 2020/2/9 下午3:45
 */
public final class CmdHandlerFactory {

    private static Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> handlerMap = new HashMap<>();

    private CmdHandlerFactory() {

    }

    public static void init() {
        handlerMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        handlerMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
    }

    /**
     * 创建CmdHandler
     * @param msgClazz
     * @return
     */
    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz) {
        if (null == msgClazz) {
            return null;
        }
        return handlerMap.get(msgClazz);
    }
}
