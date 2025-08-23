package cn.heycm.d3framework.ds.aspect;

import cn.heycm.d3framework.core.context.TenantContextHolder;
import cn.heycm.d3framework.core.contract.constant.AppConstant;
import cn.heycm.d3framework.core.utils.Assert;
import cn.heycm.d3framework.ds.tenant.Tenant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 租户切面，设置租户上下文，设置切面最高优先级（否则与事务注解在同一个方法使用时会失效）
 * @author heycm
 * @version 1.0
 * @since 2025/3/27 21:19
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TenantAspect {

    @Around("@annotation(cn.heycm.d3framework.ds.tenant.Tenant)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String tenantId = getTenantId(joinPoint);
        Assert.notBlank(tenantId, "Tenant context is missing.");
        try {
            TenantContextHolder.setTenantId(tenantId);
            return joinPoint.proceed();
        } finally {
            TenantContextHolder.clear();
        }
    }

    /**
     * 获取租户ID
     */
    private String getTenantId(ProceedingJoinPoint joinPoint) {
        String tenantId = getSpecifiedTenantId(joinPoint);
        if (StringUtils.hasText(tenantId)) {
            return tenantId;
        }
        return getRequestTenantId();
    }

    /**
     * 获取注解指定的租户ID
     */
    private String getSpecifiedTenantId(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Tenant tenant = signature.getMethod().getAnnotation(Tenant.class);
        return tenant.value();
    }

    /**
     * 获取请求头中租户ID
     */
    private String getRequestTenantId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(AppConstant.TENANT_ID);
    }
}
