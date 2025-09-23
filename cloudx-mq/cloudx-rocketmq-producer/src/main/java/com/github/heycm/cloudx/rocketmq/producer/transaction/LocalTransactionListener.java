package com.github.heycm.cloudx.rocketmq.producer.transaction;

import com.github.heycm.cloudx.core.utils.Jackson;
import com.github.heycm.cloudx.mq.core.contract.TransactionStatus;
import com.github.heycm.cloudx.mq.core.event.Event;
import com.github.heycm.cloudx.mq.core.transaction.TransactionHandler;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 本地事务监听器
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 22:01
 */
public class LocalTransactionListener implements TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        Event event = convert(msg);
        TransactionHandler transactionHandler = TransactionHandlerManager.get(event);
        TransactionStatus transactionStatus = transactionHandler.execute(event);
        return convert(transactionStatus);
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Event event = convert(msg);
        TransactionHandler transactionHandler = TransactionHandlerManager.get(event);
        TransactionStatus transactionStatus = transactionHandler.check(event);
        return convert(transactionStatus);
    }

    private static Event convert(Message message) {
        return Jackson.toObject(message.getBody(), Event.class);
    }

    private static LocalTransactionState convert(TransactionStatus status) {
        return switch (status) {
            case COMMIT -> LocalTransactionState.COMMIT_MESSAGE;
            case ROLLBACK -> LocalTransactionState.ROLLBACK_MESSAGE;
            default -> LocalTransactionState.UNKNOW;
        };
    }
}
