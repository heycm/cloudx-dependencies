package com.github.heycm.cloudx.rocketmq.producer.servie;

import com.github.heycm.cloudx.mq.core.callback.EventSendCallback;
import com.github.heycm.cloudx.mq.core.contract.Constant;
import com.github.heycm.cloudx.mq.core.event.Event;
import com.github.heycm.cloudx.mq.core.service.EventService;
import com.github.heycm.cloudx.rocketmq.producer.callback.SendCallbackWrapper;
import com.github.heycm.cloudx.rocketmq.producer.transaction.TransactionHandlerManager;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

/**
 * 基于RocketMQ的事件消息实现
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 22:30
 */
public class RocketMQEventServiceImpl implements EventService {

    private final RocketMQTemplate rocketMQTemplate;

    public RocketMQEventServiceImpl(RocketMQTemplate rocketMQTemplate, TransactionListener transactionListener) {
        this.rocketMQTemplate = rocketMQTemplate;
        TransactionMQProducer producer = (TransactionMQProducer) rocketMQTemplate.getProducer();
        producer.setTransactionListener(transactionListener);
    }

    @Override
    public boolean push(Event event) {
        String topic = buildTopicWithTags(event);
        SendResult sendResult = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(event).build(), event.getTimeout());
        return unwrapResult(sendResult);
    }

    @Override
    public boolean pushTransaction(Event event) {
        TransactionHandlerManager.register(event);
        String topic = buildTopicWithTags(event);
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(topic, MessageBuilder.withPayload(event).build(), "");
        return unwrapResult(sendResult);
    }

    @Override
    public boolean pushBroadcast(Event event) {
        TransactionHandlerManager.register(event);
        String topic = event.getTopic() + Constant.BROADCAST_SUFFIX;
        topic = buildTopicWithTags(topic, event.getTags());
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(topic, MessageBuilder.withPayload(event).build(), "");
        return unwrapResult(sendResult);
    }

    @Override
    public boolean pushDelay(Event event) {
        String topic = buildTopicWithTags(event);
        SendResult sendResult = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(event).build(), event.getTimeout(),
                event.getDelay());
        return unwrapResult(sendResult);
    }

    @Override
    public boolean pushOrderly(Event event) {
        String topic = event.getTopic() + Constant.ORDERLY_SUFFIX;
        topic = buildTopicWithTags(topic, event.getTags());
        SendResult sendResult = rocketMQTemplate.syncSendOrderly(topic, MessageBuilder.withPayload(event).build(), event.getHash(),
                event.getTimeout());
        return unwrapResult(sendResult);
    }

    @Override
    public void pushAsync(Event event, EventSendCallback callback) {
        String topic = buildTopicWithTags(event);
        SendCallbackWrapper sendCallback = new SendCallbackWrapper(callback);
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(event).build(), sendCallback);
    }

    @Override
    public void pushDelayAsync(Event event, EventSendCallback callback) {
        String topic = buildTopicWithTags(event);
        SendCallbackWrapper sendCallback = new SendCallbackWrapper(callback);
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(event).build(), sendCallback, event.getTimeout(), event.getDelay());
    }

    @Override
    public void pushOrderlyAsync(Event event, EventSendCallback callback) {
        String topic = event.getTopic() + Constant.ORDERLY_SUFFIX;
        topic = buildTopicWithTags(topic, event.getTags());
        SendCallbackWrapper sendCallback = new SendCallbackWrapper(callback);
        rocketMQTemplate.asyncSendOrderly(topic, MessageBuilder.withPayload(event).build(), event.getHash(), sendCallback,
                event.getTimeout());
    }

    private static String buildTopicWithTags(Event event) {
        return buildTopicWithTags(event.getTopic(), event.getTags());
    }

    private static String buildTopicWithTags(String topic, String tags) {
        return StringUtils.hasText(tags) ? topic + ":" + tags : topic;
    }


    private static boolean unwrapResult(SendResult sendResult) {
        if (sendResult instanceof TransactionSendResult tsr) {
            return SendStatus.SEND_OK == sendResult.getSendStatus()
                    && LocalTransactionState.COMMIT_MESSAGE == tsr.getLocalTransactionState();
        }
        return SendStatus.SEND_OK == sendResult.getSendStatus();
    }
}
