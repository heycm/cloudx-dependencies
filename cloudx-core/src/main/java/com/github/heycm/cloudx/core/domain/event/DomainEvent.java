package com.github.heycm.cloudx.core.domain.event;

import com.github.heycm.cloudx.core.context.SpringContext;
import com.github.heycm.cloudx.core.context.TenantContextHolder;
import java.util.Map;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 领域事件
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 16:53
 */
@Getter
public abstract class DomainEvent extends ApplicationEvent {

    /**
     * 租户ID
     */
    private final String tenantId;

    /**
     * 本地线程变量值，用于跨线程传递信息
     */
    private Map<String, Object> threadContextValue;

    protected <T> DomainEvent(T source) {
        super(source);
        this.tenantId = TenantContextHolder.getTenantId();
        // this.threadContextValue = ThreadContext.get();
    }

    /**
     * 获取事件源对象
     * @return 事件源对象
     */
    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) super.getSource();
    }

    /**
     * 获取事件源对象
     * @param clazz 事件源对象类型
     * @return 事件源对象
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) super.getSource();
    }

    /**
     * 发布事件，同步执行
     */
    public void publish() {
        SpringContext.publish(this);
    }

    /**
     * 发布事件，异步执行
     */
    public void publishAsync() {
        AsyncDomainEventBridger bridger = SpringContext.getBean(AsyncDomainEventBridger.class);
        bridger.async(this);
    }

    /**
     * 延迟发布事件，异步执行
     * @param delay 延迟时间，单位毫秒
     */
    public void publish(long delay) {
        AsyncDomainEventBridger bridger = SpringContext.getBean(AsyncDomainEventBridger.class);
        bridger.schedule(this, delay);
    }
}
