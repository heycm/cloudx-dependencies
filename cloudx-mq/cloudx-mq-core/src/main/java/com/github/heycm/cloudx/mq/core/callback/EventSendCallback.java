package com.github.heycm.cloudx.mq.core.callback;

/**
 * 异步消息发送回调
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 22:14
 */
public interface EventSendCallback {

    /**
     * 发送成功的回调
     * @param sendResult 发送结果
     */
    <T> void onSuccess(T sendResult);

    /**
     * 发送失败的回调
     * @param e     异常
     */
    void onException(Throwable e);
}
