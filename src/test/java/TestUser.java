import java.util.concurrent.atomic.AtomicInteger;

public class TestUser {

    public volatile int currentHp;

    public AtomicInteger atomicHp;
    /**
     * 加锁实现
     * @param delHp
     */
    public synchronized void substractHp(int delHp) {

        if (delHp <= 0) {
            return;
        }

        currentHp = currentHp -delHp;
    }

    public synchronized void attkUser(TestUser targetUser) {

        if (null == targetUser) {
            return;
        }

        int attkDmg;

//        synchronized (this) {
            // 在这里计算我的攻击、伤害,
            // 注意只有降低锁的粒度才能避免死锁的产生...
            // 可是这样做的话,
            // 就和游戏的业务逻辑纠缠在一起了!
            // 游戏业务逻辑时而简单时而复杂,
            // 加锁粒度时而调高时而调低,
            // 开发难度实在是太大了...
            // 最后在线上运行的时候, 我们往往分不清:
            // -- 是业务逻辑调用错误导致的死锁?
            // -- 还是死锁导致的业务逻辑错误?
            attkDmg = 10;
//        }

        targetUser.substractHp(attkDmg);

    }

}
