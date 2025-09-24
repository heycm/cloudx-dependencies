package com.github.heycm.cloudx.rocketmq.consumer.listener;

import com.github.heycm.cloudx.mq.core.contract.Constant;
import com.github.heycm.cloudx.mq.core.event.Event;
import com.github.heycm.cloudx.rocketmq.consumer.dispatcher.EventDispatcher;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * 广播消息监听
 * @author heycm
 * @version 1.0
 * @since 2025/9/24 12:04
 */
@RocketMQMessageListener(topic = "${spring.application.name}" + Constant.BROADCAST_SUFFIX, consumerGroup = "${spring.application.name}"
        + Constant.BROADCAST_SUFFIX, messageModel = MessageModel.BROADCASTING)
public class BroadcastListener implements RocketMQListener<Event> {

    @Override
    public void onMessage(Event message) {
        EventDispatcher.onEvent(message);
    }
}
