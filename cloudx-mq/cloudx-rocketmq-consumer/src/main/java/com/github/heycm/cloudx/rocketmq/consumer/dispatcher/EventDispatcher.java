package com.github.heycm.cloudx.rocketmq.consumer.dispatcher;

import com.github.heycm.cloudx.core.context.TenantContextHolder;
import com.github.heycm.cloudx.core.contract.constant.AppConstant;
import com.github.heycm.cloudx.mq.core.event.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/**
 * 事件分发器
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 23:19
 */
@Slf4j
public class EventDispatcher {

    /**
     * 事件处理器对象：eventName -> handler
     */
    private static final Map<String, Object> HANDLERS = new ConcurrentHashMap<>(128);

    /**
     * 事件触发方法：eventName -> method
     */
    private static final Map<String, Method> METHODS = new ConcurrentHashMap<>(128);

    /**
     * 注册事件处理者
     * @param handler 事件处理器
     */
    public static void registerHandler(Object handler) {
        Class<?> clazz = handler.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            // eventName 只能注册一次
            String eventName = method.getName();
            if (METHODS.containsKey(eventName)) {
                throw new RuntimeException("Duplicate event handler method: " + eventName);
            }
            // 方法签名判断
            if (!isValidMethodSignature(method)) {
                continue;
            }
            METHODS.put(eventName, method);
            HANDLERS.put(eventName, handler);
        }
    }

    /**
     * 事件消费
     * @param event 事件
     */
    public static void onEvent(Event event) {
        setContext(event);
        clearContext(event);
        try {
            Method method = METHODS.get(event.getEventName());
            Object handler = HANDLERS.get(event.getEventName());
            if (method == null || handler == null) {
                log.warn("Unsupported topic event: {} {}", event.getTopic(), event.getEventName());
                return;
            }
            method.invoke(handler, event);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            clearContext(event);
        }
    }

    /**
     * 设置上下文数据
     */
    private static void setContext(Event event) {
        MDC.put(AppConstant.TRACE_ID, event.getEventId());
        MDC.put(AppConstant.UID, event.getTraceUid());
        MDC.put(AppConstant.TENANT_ID, event.getTenantId());
        if (StringUtils.hasText(event.getTenantId())) {
            TenantContextHolder.setTenantId(event.getTenantId());
        }
    }

    /**
     * 清除上下文数据
     */
    private static void clearContext(Event event) {
        MDC.remove(AppConstant.TRACE_ID);
        MDC.remove(AppConstant.UID);
        MDC.remove(AppConstant.TENANT_ID);
        if (StringUtils.hasText(event.getTenantId())) {
            TenantContextHolder.clear();
        }
    }

    /**
     * 校验方法签名，仅注册符合 public void xxx(Event event) 签名的方法
     */
    private static boolean isValidMethodSignature(Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(void.class) && method.getParameterCount() == 1
                && Event.class.isAssignableFrom(method.getParameterTypes()[0]);
    }
}
