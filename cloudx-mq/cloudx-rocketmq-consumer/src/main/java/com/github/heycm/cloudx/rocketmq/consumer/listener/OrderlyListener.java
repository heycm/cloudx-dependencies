package com.github.heycm.cloudx.rocketmq.consumer.listener;

import com.github.heycm.cloudx.mq.core.contract.Constant;
import com.github.heycm.cloudx.mq.core.event.Event;
import com.github.heycm.cloudx.rocketmq.consumer.dispatcher.EventDispatcher;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * 顺序消息监听
 * @author heycm
 * @version 1.0
 * @since 2025/9/24 12:06
 */
@RocketMQMessageListener(topic = "${spring.application.name}" + Constant.ORDERLY_SUFFIX, consumerGroup = "${spring.application.name}"
        + Constant.ORDERLY_SUFFIX, consumeMode = ConsumeMode.ORDERLY)
public class OrderlyListener implements RocketMQListener<Event> {

    @Override
    public void onMessage(Event message) {
        EventDispatcher.onEvent(message);
    }
}
