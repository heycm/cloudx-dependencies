package com.github.heycm.cloudx.lock.annotation;

import com.github.heycm.cloudx.core.contract.result.ResultCode;
import com.github.heycm.cloudx.lock.LockX;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 锁注解
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 22:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lock {

    /**
     * 锁前缀
     */
    String prefix() default "";

    /**
     * 解析动态锁key值，格式：[参数名].[字段名]，如 user.id
     */
    String key();

    /**
     * 锁过期时间，毫秒
     */
    long leaseTime() default LockX.DEFAULT_LEASE_TIME;

    /**
     * 获取锁最大等待时间，毫秒
     */
    long waitTime() default LockX.DEFAULT_WAIT_TIME;

    /**
     * 加锁失败时返回的错误码
     */
    ResultCode error() default ResultCode.LOCK_FAILED;

}
