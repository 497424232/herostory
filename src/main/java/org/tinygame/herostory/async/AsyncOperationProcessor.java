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
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("AsyncOperationProcessor");
        return thread;
    });

    private AsyncOperationProcessor() {

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

        this.executorService.submit(() ->{
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
