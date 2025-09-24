package com.github.heycm.cloudx.rocketmq.producer.servie;

import com.github.heycm.cloudx.mq.core.event.Event;
import org.apache.rocketmq.client.producer.SendCallback;

/**
 * 事件推送服务
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 22:23
 */
public interface EventService {

    /**
     * 推送普通事件
     * @param event 事件
     * @return 是否成功
     */
    boolean push(Event event);

    /**
     * 推送事务事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushTransaction(Event event);

    /**
     * 推送广播事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushBroadcast(Event event);

    /**
     * 推送延迟事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushDelay(Event event);

    /**
     * 推送有序事件
     * @param event 事件
     * @return 是否成功
     */
    boolean pushOrderly(Event event);

    /**
     * 异步推送事件
     * @param event        事件
     * @param sendCallback 发送结果回调
     */
    void pushAsync(Event event, SendCallback sendCallback);

    /**
     * 异步推送延迟事件
     * @param event        事件
     * @param sendCallback 发送结果回调
     */
    void pushDelayAsync(Event event, SendCallback sendCallback);

    /**
     * 异步推送顺序事件
     * @param event        事件
     * @param sendCallback 发送结果回调
     */
    void pushOrderlyAsync(Event event, SendCallback sendCallback);

}
