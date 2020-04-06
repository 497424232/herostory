package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.login.LoginService;
import org.tinygame.herostory.login.db.UserEntity;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @auther changmk
 * @date 2020/2/9 下午3:18
 */
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd>{

    private final Logger logger = LoggerFactory.getLogger(UserEntryCmdHandler.class);
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {
        // 从指令对象中获取用户id和英雄形象
        GameMsgProtocol.UserEntryCmd cmd = msg;

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        User existUser = UserManager.getUserById(userId);
        if (null == existUser) {
            logger.error("用户不存在，userId={}", userId);
            return;
        }

        String heroAvatar = existUser.getHeroAvatar();

        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        //将用户加入字典
//        User newUser = new User();
//        newUser.setUserId(userId);
//        newUser.setHeroAvatar(avatar);
//        newUser.setCurrentHp(100);
//        newUser.setMoveState(new MoveState());
//
//        UserManager.addUser(newUser);
//
//        // 将用户id 附着在channel
//        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        // 构建结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
