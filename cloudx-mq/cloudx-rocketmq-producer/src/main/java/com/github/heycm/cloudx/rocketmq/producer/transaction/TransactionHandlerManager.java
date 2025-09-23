package com.github.heycm.cloudx.rocketmq.producer.transaction;

import com.github.heycm.cloudx.mq.core.contract.Constant;
import com.github.heycm.cloudx.mq.core.event.Event;
import com.github.heycm.cloudx.mq.core.transaction.TransactionHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地事务管理器
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 21:48
 */
public class TransactionHandlerManager {

    private static final Map<String, TransactionHandler> HANDLER_MAP = new ConcurrentHashMap<>();

    private TransactionHandlerManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void register(Event event) {
        assert event.getTransactionHandler() != null;
        HANDLER_MAP.put(wrapKey(event), event.getTransactionHandler());
    }

    public static TransactionHandler get(Event event) {
        return HANDLER_MAP.get(wrapKey(event));
    }

    private static String wrapKey(Event event) {
        return event.getTopic() + Constant.EVENT_SEPARATOR + event.getEventName();
    }
}
