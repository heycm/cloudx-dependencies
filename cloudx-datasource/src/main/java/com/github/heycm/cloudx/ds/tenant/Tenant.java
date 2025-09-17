package com.github.heycm.cloudx.ds.tenant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 租户注解，从请求头中获取租户ID并设置租户数据源
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Tenant {

    /**
     * 租户ID
     * @return 租户ID
     */
    String value() default "";
}
