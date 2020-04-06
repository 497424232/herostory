package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.login.LoginService;
import org.tinygame.herostory.login.db.UserEntity;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.Iterator;
import java.util.Map;

/**
 * 用户登录消息类
 */
public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd> {

    private final Logger logger = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd msg) {
        if (null == ctx || null == msg) {
            return;
        }

        UserEntity userEntity = null;
        try {
            userEntity = LoginService.getInstance().userLogin(msg.getUserName(), msg.getPassword());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        if (null == userEntity) {
            logger.error("用户登录失败，userName={}", msg.getUserName());
            return;
        }

        User user = new User();
        user.setUserId(userEntity.userId);
        user.setUserName(userEntity.userName);
        user.setHeroAvatar(userEntity.heroAvatar);
        user.setCurrentHp(100);
        user.setMoveState(new MoveState());

        // 添加user
        UserManager.addUser(user);

        //将用户id附着到channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(user.getUserId());

        GameMsgProtocol.UserLoginResult.Builder resultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();
        resultBuilder.setHeroAvatar(user.getHeroAvatar());
        resultBuilder.setUserName(user.getUserName());
        resultBuilder.setUserId(user.getUserId());

        // 构建结果并发送
        GameMsgProtocol.UserLoginResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);

    }

    /**
     * 清理超时的登陆时间
     *
     * @param userLoginTimeMap 用户登陆时间字典
     */
    static private void clearTimeoutLoginTime(Map<String, Long> userLoginTimeMap) {
        if (null == userLoginTimeMap ||
                userLoginTimeMap.isEmpty()) {
            return;
        }

        // 获取系统时间
        final long currTime = System.currentTimeMillis();
        // 获取迭代器
        Iterator<String> it = userLoginTimeMap.keySet().iterator();

        while (it.hasNext()) {
            // 根据用户名称获取登陆时间
            String userName = it.next();
            Long loginTime = userLoginTimeMap.get(userName);

            if (null == loginTime ||
                    currTime - loginTime > 5000) {
                // 如果已经超时,
                it.remove();
            }
        }
    }
}
