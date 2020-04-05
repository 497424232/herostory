package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @auther changmk
 * @date 2020/2/9 下午3:19
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd cmd) {
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
        for (User currentUser : UserManager.listUser()) {
            if (null == currentUser) {
                continue;
            }

            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(currentUser.getUserId());
            userInfoBuilder.setHeroAvatar(currentUser.getHeroAvatar());

            //构建用户移动状态
            MoveState moveState = currentUser.getMoveState();
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder moveStateBuilder
                    = GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();

            moveStateBuilder.setFromPosX(moveState.fromPosX);
            moveStateBuilder.setFromPosY(moveState.fromPosY);
            moveStateBuilder.setToPosX(moveState.toPosX);
            moveStateBuilder.setToPosY(moveState.toPosY);
            moveStateBuilder.setStartTime(moveState.startTime);

            //将移动状态传递给移动用户
            userInfoBuilder.setMoveState(moveStateBuilder);

            resultBuilder.addUserInfo(userInfoBuilder);
        }
        GameMsgProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }
}
