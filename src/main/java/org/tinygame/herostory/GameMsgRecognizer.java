package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther changmk
 * @date 2020/2/9 下午4:32
 */
public class GameMsgRecognizer {

    private static Logger logger = LoggerFactory.getLogger(GameMsgRecognizer.class);

    /**
     * 消息代码和消息体字典
     */
    private static final Map<Integer, GeneratedMessageV3> msgCodeAndMsgBodyMap = new HashMap<>();

    /**
     * 消息类型和消息编号字典
     */
    private static final Map<Class<?>, Integer> msgClazzAndMsgCodeMap = new HashMap<>();

    private GameMsgRecognizer() {

    }

    static public void init() {

        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();

        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                continue;
            }

            String clazzName = innerClazz.getSimpleName();
            clazzName = clazzName.toLowerCase();

            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
                String strMsgCode = msgCode.name();
                strMsgCode = strMsgCode.replaceAll("_", "");
                strMsgCode = strMsgCode.toLowerCase();

                if (!strMsgCode.startsWith(clazzName)) {
                    continue;
                }

                try {
                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);

                    logger.info("{}<==>{}", innerClazz.getName(), msgCode.getNumber());

                    msgCodeAndMsgBodyMap.put(
                            msgCode.getNumber(),
                            (GeneratedMessageV3) returnObj
                    );

                    msgClazzAndMsgCodeMap.put(
                            innerClazz,
                            msgCode.getNumber()
                    );
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }

//        msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE, GameMsgProtocol.UserEntryCmd.getDefaultInstance());
//        msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
//        msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE, GameMsgProtocol.UserMoveToCmd.getDefaultInstance());

//        msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class, GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
//        msgClazzAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class, GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
//        msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class, GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
//        msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class, GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
    }

    public static Message.Builder getBuilderByMsgCode(int msgCode) {

        if (msgCode < 0) {
            return null;
        }

        GeneratedMessageV3 msg = msgCodeAndMsgBodyMap.get(msgCode);

        if (msg == null) {
            return null;
        }

        return msg.newBuilderForType();
    }

    public static int getMsgCodeByMsgClazz(Class<?> msgClazz) {

        if (null == msgClazz) {
            return -1;
        }

        Integer msgCode = msgClazzAndMsgCodeMap.get(msgClazz);

        if (null != msgCode) {
            return msgCode.intValue();
        } else {
            return -1;
        }
    }
}
