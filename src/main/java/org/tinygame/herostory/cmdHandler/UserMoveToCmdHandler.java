package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @auther changmk
 * @date 2020/2/9 下午3:19
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{

    private final Logger logger = LoggerFactory.getLogger(UserMoveToCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if (null == userId) {
            return;
        }

        User moveUser = UserManager.getUserById(userId);
        if (null == moveUser) {
            logger.error("移动用户未找到,userId = {}", userId);
            return;
        }
        //获取移动状态
        MoveState moveState = moveUser.getMoveState();
        moveState.fromPosX = msg.getMoveFromPosX();
        moveState.fromPosY = msg.getMoveFromPosY();
        moveState.toPosX = msg.getMoveToPosX();
        moveState.toPosY = msg.getMoveToPosY();

        moveState.startTime = System.currentTimeMillis();

        GameMsgProtocol.UserMoveToCmd cmd = msg;

        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
//        resultBuilder.setMoveToPosX(cmd.getMoveToPosX());
//        resultBuilder.setMoveToPosY(cmd.getMoveToPosY());
        resultBuilder.setMoveFromPosX(moveState.fromPosX);
        resultBuilder.setMoveFromPosY(moveState.fromPosY);
        resultBuilder.setMoveToPosX(moveState.toPosX);
        resultBuilder.setMoveToPosY(moveState.toPosY);
        resultBuilder.setMoveStartTime(moveState.startTime);

        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

}
