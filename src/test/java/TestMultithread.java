import java.util.concurrent.atomic.AtomicInteger;

public class TestMultithread {
    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            System.out.println("第"+i+"次测试：");
            new TestMultithread().test4();
        }

    }

    public void test1() {
        TestUser testUser = new TestUser();
        testUser.currentHp = 100;

        Thread[] threads = new Thread[2];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                testUser.currentHp = testUser.currentHp - 10;
            }, "thread" + i);
        }

        for (Thread thread: threads) {
            thread.start();
        }

        try {
            for (Thread thread: threads) {
                thread.join();
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }

        if (testUser.currentHp != 80) {
            throw new RuntimeException("当前血量错误， currentHp = " + testUser.currentHp );
        }
        System.out.println("当前血量正确， currentHp = " + testUser.currentHp);
    }

    /**
     * 测试volatile + synchronized
     */
    public void test2() {
        TestUser testUser = new TestUser();
        testUser.currentHp = 100;

        Thread[] threads = new Thread[2];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                testUser.substractHp(10);
            }, "thread" + i);
        }

        for (Thread thread: threads) {
            thread.start();
        }

        try {
            for (Thread thread: threads) {
                thread.join();
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }

        if (testUser.currentHp != 80) {
            throw new RuntimeException("当前血量错误， currentHp = " + testUser.currentHp );
        }
        System.out.println("当前血量正确， currentHp = " + testUser.currentHp);
    }

    /**
     * 测试死锁，自己电脑未验证成功
     */
    public void test3() {
        TestUser testUser = new TestUser();
        testUser.currentHp = 100;
        TestUser testUser2 = new TestUser();
        testUser2.currentHp = 100;

        Thread[] threads = new Thread[2];

        threads[0] = new Thread(()->{
            testUser.attkUser(testUser2);
        }, "thread" + 0);
        threads[1] = new Thread(()->{
            testUser2.attkUser(testUser);
        }, "thread" + 1);

        for (Thread thread: threads) {
            thread.start();
        }

        try {
            for (Thread thread: threads) {
                thread.join();
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }

        System.out.println("攻击完成");
    }

    /**
     * 测试atomic
     */
    public void test4() {
        TestUser testUser = new TestUser();
        testUser.atomicHp = new AtomicInteger(100);

        Thread[] threads = new Thread[2];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(()->{
                testUser.atomicHp.addAndGet(-10);
            }, "thread" + i);
        }

        for (Thread thread: threads) {
            thread.start();
        }

        try {
            for (Thread thread: threads) {
                thread.join();
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }

        if (testUser.atomicHp.get() != 80) {
            throw new RuntimeException("当前血量错误， currentHp = " + testUser.atomicHp.get() );
        }
        System.out.println("当前血量正确， currentHp = " + testUser.atomicHp.get());
    }

}
