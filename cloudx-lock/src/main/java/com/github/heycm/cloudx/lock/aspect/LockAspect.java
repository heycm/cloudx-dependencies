package com.github.heycm.cloudx.lock.aspect;

import com.github.heycm.cloudx.core.utils.Assert;
import com.github.heycm.cloudx.lock.annotation.Lock;
import com.github.heycm.cloudx.lock.service.LockService;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

/**
 * 锁注解处理切面
 * @author heycm
 * @version 1.0
 * @since 2025/4/2 22:58
 */
@Aspect
@Slf4j
public class LockAspect {

    private final LockService lockService;

    public LockAspect(LockService lockService) {
        this.lockService = lockService;
    }

    @Around("@annotation(com.github.heycm.cloudx.lock.annotation.Lock)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        String key = getKey(lock, method, pjp);

        boolean locked = lockService.tryLock(key, lock.leaseTime(), lock.waitTime());
        Assert.isTrue(locked, lock.error());
        try {
            return pjp.proceed();
        } finally {
            lockService.release();
        }
    }

    private String getKey(Lock lock, Method method, ProceedingJoinPoint pjp) throws NoSuchFieldException, IllegalAccessException {
        StringBuilder lockKey = new StringBuilder();
        if (StringUtils.hasText(lock.prefix())) {
            lockKey.append(lock.prefix());
        } else {
            lockKey.append(method.getClass().getName()).append("#").append(method.getName());
        }
        if (StringUtils.hasText(lock.key())) {
            String[] keys = lock.key().split("\\.");
            Parameter[] parameters = method.getParameters();
            Object[] args = pjp.getArgs();
            String value = getValue(keys, parameters, args);
            if (StringUtils.hasText(value)) {
                lockKey.append(":").append(value);
            }
        }
        return lockKey.toString();
    }

    private String getValue(String[] keys, Parameter[] parameters, Object[] args) throws NoSuchFieldException, IllegalAccessException {
        if (keys == null || parameters == null || args == null) {
            return null;
        }

        Object arg = null;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            if (i == 0) {
                for (int j = 0; j < parameters.length; j++) {
                    if (key.equals(parameters[j].getName())) {
                        if (args.length <= j) {
                            return null;
                        }
                        arg = args[j];
                        break;
                    }
                }
            } else {
                if (arg == null) {
                    return null;
                }
                Field field = arg.getClass().getDeclaredField(key);
                field.setAccessible(true);
                arg = field.get(arg);
            }
        }

        return arg == null ? null : arg.toString();
    }
}
