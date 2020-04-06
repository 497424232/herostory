package org.tinygame.herostory.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MainThreadProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步处理器
 */
public class AsyncOperationProcessor {

    private final Logger logger = LoggerFactory.getLogger(AsyncOperationProcessor.class);

    private static final AsyncOperationProcessor instance = new AsyncOperationProcessor();

    /**
     * 单线程池 AsyncOperationProcessor
     *
     * 修改为多线程处理，长度为8
     */
//    private ExecutorService executorService = Executors.newFixedThreadPool(8);
        /*Executors.newSingleThreadEx、ecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("AsyncOperationProcessor");
        return thread;
    });*/

    /**
     * 将单线程修改为单线程数组，长度可以选择根据CPU 核数定
     */
    private ExecutorService[] esArray = new ExecutorService[8];

    private AsyncOperationProcessor() {
        for (int i = 0; i < esArray.length; i++) {
            // 定义线程池名称
            final String threadName = "AsyncOperationProcessor_" + i;
            esArray[i] = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r);
                thread.setName(threadName);
                return thread;
            });
            
        }
    }

    public static AsyncOperationProcessor getInstance() {
        return instance;
    }

    /**
     * 处理异步操作
     *
     * @param asyncOperation
     */
    public void process(IAsyncOperation asyncOperation) {
        if (null == asyncOperation) {
            return;
        }

        //todo 根据bindId 取模作为使用线程池的规则
        int bindId = Math.abs(asyncOperation.bindId());
        int bindIndex = bindId % this.esArray.length;

        this.esArray[bindIndex].submit(() ->{
            try {
                //执行异步操作
                asyncOperation.doAsync();
                //返回主线程执行完成逻辑
                //todo 巧妙运用asyncOperation 这个接口处理两个线程池之间的执行顺序
                MainThreadProcessor.getInstance().process(asyncOperation::doFinish);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }

        });
    }

}
