package com.github.heycm.cloudx.rocketmq.producer.callback;

import com.github.heycm.cloudx.mq.core.callback.EventSendCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * RocketMQ 发送结果回调包装类
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 22:07
 */
@Slf4j
public class SendCallbackWrapper implements SendCallback {

    private final EventSendCallback callback;

    public SendCallbackWrapper(EventSendCallback callback) {
        assert callback != null;
        this.callback = callback;
    }

    @Override
    public void onSuccess(SendResult sendResult) {
        callback.onSuccess(sendResult);
    }

    @Override
    public void onException(Throwable e) {
        callback.onException(e);
    }
}
