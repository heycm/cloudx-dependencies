package com.github.heycm.cloudx.mq.core.transaction;

import com.github.heycm.cloudx.mq.core.contract.TransactionStatus;
import com.github.heycm.cloudx.mq.core.event.Event;

/**
 * 事务消息，本地事务处理器
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 20:42
 */
public interface TransactionHandler {

    /**
     * 向 Broker 提交 Half 消息成功后，执行本地事务
     * @param event 消息事件
     * @return 本地事务状态
     */
    TransactionStatus execute(Event event);

    /**
     * Half 消息超时没收到本地事务状态时，Broker 反查本地事务状态
     * @param event 消息事件
     * @return 本地事务状态
     */
    TransactionStatus check(Event event);
}
