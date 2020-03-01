package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.util.PackageUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 指令处理工厂
 *
 * @auther changmk
 * @date 2020/2/9 下午3:45
 */
public final class CmdHandlerFactory {

    /**
     * 处理器字典
     */
    private static Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> handlerMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(CmdHandlerFactory.class);

    private CmdHandlerFactory() {

    }

    public static void init() {

        logger.info("关联Cmd 和 handler");
        //获取包名称
        final String packageName = CmdHandlerFactory.class.getPackage().getName();
        // 获取ICmdHandler的子类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(packageName, true, ICmdHandler.class);

        for (Class clazz : clazzSet) {
            // 如果是抽象类，则不处理
            if ((clazz.getModifiers() & Modifier.ABSTRACT) != 0) {
                continue;
            }

            //获取方法数组
            Method[] methodArray = clazz.getMethods();
            // 消息类型
            Class<?> msgType = null;

            for (Method method : methodArray) {
                // 如果不是handle方法，不处理
                if (!method.getName().contains("handle")) {
                    continue;
                }

                // 获取函数参数类型
                Class<?>[] parameterTypes = method.getParameterTypes();

                //
                if (parameterTypes.length < 2 ||
                    parameterTypes[1] == GeneratedMessageV3.class ||
                    !GeneratedMessageV3.class.isAssignableFrom(parameterTypes[1]) ) {
                    continue;
                }

                msgType = parameterTypes[1];
                break;
            }

            try {
                ICmdHandler<?> newHander = (ICmdHandler<?>) clazz.newInstance();
                logger.info("{}关联{}", msgType.getName(), clazz.getName());

                handlerMap.put(msgType, newHander);

            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage(), e);
            }
        }

    }

    /**
     * 创建CmdHandler
     *
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
