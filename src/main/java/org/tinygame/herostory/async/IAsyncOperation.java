package org.tinygame.herostory.async;

/**
 * 异步操作接口
 *
 * todo 主要用于解决 主线程和 异步线程之间数据的交互问题
 */
public interface IAsyncOperation {

    /**
     * 执行异步操作
     */
    void doAsync();

    /**
     * 执行完成逻辑
     */
    default void doFinish() {
    }
}
