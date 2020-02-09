package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 指令处理工厂
 * @auther changmk
 * @date 2020/2/9 下午3:45
 */
public final class CmdHandlerFactory {

    private CmdHandlerFactory() {

    }

    /**
     * 创建CmdHandler
     * @param msg
     * @return
     */
    public static ICmdHandler<? extends GeneratedMessageV3> create(Object msg) {
        if (msg instanceof GameMsgProtocol.UserEntryCmd) {
            return new UserEntryCmdHandler();
        } else if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            return new WhoElseIsHereCmdHandler();
        } else if (msg instanceof GameMsgProtocol.UserMoveToCmd) {
            return new UserMoveToCmdHandler();
        } else {
            return null;
        }
    }
}
