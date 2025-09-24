package com.github.heycm.cloudx.rocketmq.consumer;

import com.github.heycm.cloudx.rocketmq.consumer.annotation.EventHandler;
import com.github.heycm.cloudx.rocketmq.consumer.dispatcher.EventDispatcher;
import com.github.heycm.cloudx.rocketmq.consumer.listener.BroadcastListener;
import com.github.heycm.cloudx.rocketmq.consumer.listener.ClusterListener;
import com.github.heycm.cloudx.rocketmq.consumer.listener.OrderlyListener;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ROcketMQ 消费者配置
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 23:17
 */
@Configuration
@ConditionalOnProperty(value = "rocketmq.name-server")
public class RocketMQConsumerAutoConfiguration {

    /**
     * 获取所有事件处理器
     * @param applicationContext
     * @return
     */
    @Bean(name = "eventHandlers")
    public List<Object> eventHandlers(ApplicationContext applicationContext) {
        String[] names = applicationContext.getBeanNamesForAnnotation(EventHandler.class);
        List<Object> handlers = new ArrayList<>(names.length);
        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            handlers.add(bean);

            // 消息处理器注册
            EventDispatcher.registerHandler(bean);
        }
        return handlers;
    }

    /**
     * 普通消息集群监听，要求 EventDispatcher 已经初始化
     */
    @Bean
    @ConditionalOnBean(name = "eventHandlers")
    public ClusterListener clusterListener() {
        return new ClusterListener();
    }

    /**
     * 广播消费监听器，要求 EventDispatcher 已经初始化
     */
    @Bean
    @ConditionalOnBean(name = "eventHandlers")
    public BroadcastListener broadcastListener() {
        return new BroadcastListener();
    }

    /**
     * 有序消费监听器，要求 EventDispatcher 已经初始化
     */
    @Bean
    @ConditionalOnBean(name = "eventHandlers")
    public OrderlyListener orderlyListener() {
        return new OrderlyListener();
    }
}
