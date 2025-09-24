package com.github.heycm.cloudx.rocketmq.consumer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

/**
 * 事件处理器Bean注解
 * @author heycm
 * @version 1.0
 * @since 2025/9/24 11:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface EventHandler {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
