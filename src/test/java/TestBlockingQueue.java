import java.util.concurrent.*;

/**
 * 模拟线程池
 *
 * @auther changmk
 * @date 2020/3/7 下午11:24
 */
public class TestBlockingQueue {

    public static void main(String[] args) {
        TestBlockingQueue test = new TestBlockingQueue();
        test.test2();
    }

    /**
     * 测试阻塞队列
     */
    private void test1() {
        final BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
        // 线程1 往队列里放数
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++ ) {
                try {
                    Thread.sleep(300);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                blockingQueue.offer(i);
            }
        });
        // 线程2 往队列中放数
        Thread thread2 = new Thread(() -> {
            for (int i = 10; i < 20; i++ ) {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                blockingQueue.offer(i);
            }
        });
        // 线程3 在队列中取数
        Thread thread3 = new Thread(() -> {
            while (true) {
                try {
                    Integer val = blockingQueue.take();
                    System.out.println("取出数据为：" + val);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * 测试调用自己的ExecutorService
     */
    private void test2() {
        MyExecutorService myExecutorService = new MyExecutorService();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++ ) {
                try {
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                final int val = i;
                myExecutorService.submit(() ->{
                    System.out.println("拿到数据：" + val);

                });
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 10; i < 20; i++ ) {
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                final int val = i;
                myExecutorService.submit(() ->{
                    System.out.println("拿到数据：" + val);

                });
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 创建自己的ExecutorService
     */
    class MyExecutorService {
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue();

        Thread thread;

        MyExecutorService() {
            thread = new Thread(() -> {
                while (true) {
                    try {
                        Runnable runnable = blockingQueue.take();
                        if (runnable != null) {
                            runnable.run();
                        }
                    } catch (Exception ex) {

                    }
                }
            });
            // 注意：不要忘了启动此线程
            thread.start();
        }


        public void submit(Runnable runnable) {
            if (runnable == null) {
                return;
            }
            this.blockingQueue.offer(runnable);
        }
    }
}
