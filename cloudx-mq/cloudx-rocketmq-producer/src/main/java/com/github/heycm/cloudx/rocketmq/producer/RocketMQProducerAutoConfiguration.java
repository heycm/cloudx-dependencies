package com.github.heycm.cloudx.rocketmq.producer;

import com.github.heycm.cloudx.mq.core.transaction.DefaultTransactionHandler;
import com.github.heycm.cloudx.mq.core.transaction.TransactionHandler;
import com.github.heycm.cloudx.rocketmq.producer.callback.DefaultSendCallback;
import com.github.heycm.cloudx.rocketmq.producer.servie.EventService;
import com.github.heycm.cloudx.rocketmq.producer.servie.RocketMQEventServiceImpl;
import com.github.heycm.cloudx.rocketmq.producer.transaction.LocalTransactionListener;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * RocketMQ 生产者配置
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 21:40
 */
@Configuration
@ConditionalOnProperty(value = "rocketmq.name-server")
public class RocketMQProducerAutoConfiguration {

    /**
     * 注册默认事务处理器
     * @return
     */
    @Bean
    @Primary
    public TransactionHandler transactionHandler() {
        return new DefaultTransactionHandler();
    }

    /**
     * 注册默认发送回调
     * @return
     */
    @Bean
    @Primary
    public SendCallback sendCallback() {
        return new DefaultSendCallback();
    }

    /**
     * 本地事务监听器
     * @return
     */
    @Bean
    public TransactionListener transactionListener() {
        return new LocalTransactionListener();
    }

    /**
     * 注册 RocketMQ 消息推送服务
     * @param rocketMQTemplate
     * @param transactionListener
     * @return
     */
    @Bean
    public EventService eventService(RocketMQTemplate rocketMQTemplate, TransactionListener transactionListener) {
        return new RocketMQEventServiceImpl(rocketMQTemplate, transactionListener);
    }
}
