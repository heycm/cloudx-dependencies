package com.github.heycm.cloudx.core.context;

import lombok.extern.slf4j.Slf4j;

/**
 * 租户上下文
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 20:54
 */
@Slf4j
public class TenantContextHolder {

    private static final ThreadLocal<String> TENANT = new ThreadLocal<>();

    /**
     * 设置租户ID
     * @param tenantId 租户ID
     */
    public static void setTenantId(String tenantId) {
        if (exists()) {
            log.debug("Tenant context switches from [{}] to [{}]", TenantContextHolder.getTenantId(), tenantId);
        } else {
            log.debug("Tenant context switches to [{}]", tenantId);
        }
        TENANT.set(tenantId);
    }

    /**
     * 获取租户ID
     * @return 租户ID
     */
    public static String getTenantId() {
        return TENANT.get();
    }

    /**
     * 清除租户ID
     */
    public static void clear() {
        if (exists()) {
            log.debug("Tenant context [{}] removed.", TenantContextHolder.getTenantId());
            TENANT.remove();
        } else {
            log.warn("Tenant context has already been removed, Please check the nested set tenant.");
        }
    }

    /**
     * 判断租户ID是否存在
     * @return true-存在
     */
    public static boolean exists() {
        return TENANT.get() != null;
    }
}
