package com.github.heycm.cloudx.mq.core.transaction;

import com.github.heycm.cloudx.mq.core.contract.TransactionStatus;
import com.github.heycm.cloudx.mq.core.event.Event;

/**
 * 默认本地事务处理器，默认提交事务
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 21:23
 */
public class DefaultTransactionHandler implements TransactionHandler {

    @Override
    public TransactionStatus execute(Event event) {
        return TransactionStatus.COMMIT;
    }

    @Override
    public TransactionStatus check(Event event) {
        return TransactionStatus.COMMIT;
    }
}
