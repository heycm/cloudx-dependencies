package com.github.heycm.cloudx.rocketmq.producer.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * 默认异步事件发送回调
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 22:19
 */
@Slf4j
public class DefaultSendCallback implements SendCallback {

    @Override
    public void onSuccess(SendResult sendResult) {
        if (log.isDebugEnabled()) {
            log.debug("Send message success, sendResult: {}", sendResult);
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("Send message error: {}", e.getMessage(), e);
    }
}
