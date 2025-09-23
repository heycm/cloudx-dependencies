package com.github.heycm.cloudx.mq.core.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.heycm.cloudx.core.context.TenantContextHolder;
import com.github.heycm.cloudx.core.contract.constant.AppConstant;
import com.github.heycm.cloudx.mq.core.contract.Constant;
import com.github.heycm.cloudx.mq.core.transaction.TransactionHandler;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.slf4j.MDC;

/**
 * 消息事件模型
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 20:35
 */
@Data
public class Event implements Serializable {

    @Serial
    private static final long serialVersionUID = 2860760395449317250L;

    /**
     * 事件ID/TraceId
     */
    private String eventId;

    /**
     * TraceUID
     */
    private String traceUid;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 事件主题
     */
    private String topic;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件上下文
     */
    private Context context;

    /**
     * 事件标签
     */
    private String tags;

    /**
     * 发送超时，默认3000ms
     */
    private long timeout = 3000;

    /**
     * 延迟消息等级
     */
    private int delay;

    /**
     * 顺序消息HashKey
     */
    private String hash;

    /**
     * 本地事务处理处理，注意此字段无法序列化操作
     */
    @JsonIgnore
    private transient TransactionHandler transactionHandler;

    public Event() {
        this.eventId = MDC.get(AppConstant.TRACE_ID);
        this.traceUid = MDC.get(AppConstant.UID);
        this.tenantId = TenantContextHolder.getTenantId();
    }

    public Event(String event) {
        this();
        String[] split = event.split(Constant.EVENT_SEPARATOR);
        assert split.length == 2;
        this.topic = split[0];
        this.eventName = split[1];
    }

    /**
     * 创建主题事件
     * @param event 事件名称，格式：topic.eventName
     * @return Event
     */
    public static Event of(String event) {
        return new Event(event);
    }

    /**
     * 创建主题事件
     * @param event 事件名称，格式：topic.eventName
     * @param data  数据
     * @return Event
     */
    public static Event of(String event, String data) {
        Event e = new Event(event);
        e.setContext(Context.of(data));
        return e;
    }

    /**
     * 创建主题事件
     * @param event   事件名称，格式：topic.eventName
     * @param context 上下文
     * @return Event
     */
    public static Event of(String event, Context context) {
        Event e = new Event(event);
        e.setContext(context);
        return e;
    }

    /**
     * 创建主题事件
     * @param event              事件名称，格式：topic.eventName
     * @param transactionHandler 事务处理器
     * @return Event
     */
    public static Event of(String event, TransactionHandler transactionHandler) {
        Event e = new Event(event);
        e.setTransactionHandler(transactionHandler);
        return e;
    }

    /**
     * 创建主题事件
     * @param event              事件名称，格式：topic.eventName
     * @param data               数据
     * @param transactionHandler 事务处理器
     * @return Event
     */
    public static Event of(String event, String data, TransactionHandler transactionHandler) {
        Event e = of(event, data);
        e.setTransactionHandler(transactionHandler);
        return e;
    }

    /**
     * 创建主题事件
     * @param event              事件名称，格式：topic.eventName
     * @param context            上下文
     * @param transactionHandler 事务处理器
     * @return Event
     */
    public static Event of(String event, Context context, TransactionHandler transactionHandler) {
        Event e = of(event, context);
        e.setTransactionHandler(transactionHandler);
        return e;
    }
}
