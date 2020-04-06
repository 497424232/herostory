package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 用户攻击信息处理器
 *
 * @auther changmk
 * @date 2020/3/1 下午10:53
 */
public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {

    private final Logger logger = LoggerFactory.getLogger(UserAttkCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd msg) {
        if (ctx == null || msg == null) {
            return;
        }

        // 获取攻击者 Id
        Integer attkUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == attkUserId) {
            return;
        }

        // 获取被攻击者 Id
        int targetUserId = msg.getTargetUserId();

        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(attkUserId);
        resultBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserAttkResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);


        User targetUser = UserManager.getUserById(targetUserId);
        if (null == targetUser) {
            logger.error("未找到被攻击用户，targetUserId={}", targetUserId);
            return;
        }

        // 在此打印线程名称
        logger.info("当前线程 = {}", Thread.currentThread().getName());
        // 我们可以看到不相同的线程名称...
        // 用户 A 在攻击用户 C 的时候, 是在线程 1 里,
        // 用户 B 在攻击用户 C 的时候, 是在线程 2 里,
        // 线程 1 和线程 2 同时修改用户 C 的血量...
        // 这是要出事的节奏啊!

        // 可以根据自己的喜好写,
        // 例如加上装备加成、躲避、格挡、暴击等等...
        // 这些都属于游戏的业务逻辑了!
        int subtractHp = 10;
        targetUser.setCurrentHp(targetUser.getCurrentHp() - subtractHp);
        bradCastSubstactHp(targetUserId, subtractHp);

        if (targetUser.getCurrentHp() <= 0) {
            bradCastDie(targetUserId);
        }

    }

    /**
     * 广播掉血信息
     * @param targetUserId
     */
    private void bradCastSubstactHp(int targetUserId, int substactHp) {
        if (targetUserId < 0 || substactHp <= 0) {
            return;
        }


        GameMsgProtocol.UserSubtractHpResult.Builder subtractHpBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        subtractHpBuilder.setTargetUserId(targetUserId);
        subtractHpBuilder.setSubtractHp(substactHp);

        GameMsgProtocol.UserSubtractHpResult newResult2 = subtractHpBuilder.build();
        Broadcaster.broadcast(newResult2);
    }

    /**
     * 广播死亡信息
     *
     * @param targetUserId
     */
    private void bradCastDie(int targetUserId) {
        if (targetUserId < 0) {
            return;
        }

        GameMsgProtocol.UserDieResult.Builder dieBuilder = GameMsgProtocol.UserDieResult.newBuilder();
        dieBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserDieResult result = dieBuilder.build();
        Broadcaster.broadcast(result);

    }

}
