package cn.heycm.d3framework.core.utils;

import cn.heycm.d3framework.core.contract.exception.ServiceException;
import cn.heycm.d3framework.core.contract.result.IR;
import cn.heycm.d3framework.core.contract.result.ResultCode;
import java.util.function.Supplier;
import org.springframework.util.StringUtils;

/**
 * 断言工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 22:45
 */
public final class Assert {

    private Assert() {
    }

    public static <X extends ServiceException> void isTrue(boolean expression, Supplier<X> exception) {
        if (!expression) {
            throw exception.get();
        }
    }

    public static void isTrue(boolean expression, String errmsg, Object... args) {
        if (!expression) {
            throw new ServiceException(errmsg, args);
        }
    }

    public static void isTrue(boolean expression, ResultCode resultCode) {
        if (!expression) {
            throw new ServiceException(resultCode);
        }
    }

    public static void isOk(IR<?> r) {
        if (r == null) {
            throw new ServiceException(ResultCode.ERROR.code(), "result is null");
        }
        if (!r.isOk()) {
            throw new ServiceException(r);
        }
    }

    public static <T> void notNull(T obj, ResultCode resultCode) {
        if (obj == null) {
            throw new ServiceException(resultCode);
        }
    }

    public static <T> void notNull(T obj, String errmsg, Object... args) {
        if (obj == null) {
            throw new ServiceException(errmsg, args);
        }
    }

    public static <T> void isNull(T obj, ResultCode resultCode) {
        if (obj != null) {
            throw new ServiceException(resultCode);
        }
    }

    public static <T> void isNull(T obj, String errmsg, Object... args) {
        if (obj != null) {
            throw new ServiceException(errmsg, args);
        }
    }

    public static void notBlank(String obj, ResultCode resultCode) {
        if (!StringUtils.hasText(obj)) {
            throw new ServiceException(resultCode);
        }
    }

    public static void notBlank(String obj, String errmsg, Object... args) {
        if (!StringUtils.hasText(obj)) {
            throw new ServiceException(errmsg, args);
        }
    }

    public static void isBlank(String obj, ResultCode resultCode) {
        if (StringUtils.hasText(obj)) {
            throw new ServiceException(resultCode);
        }
    }

    public static void isBlank(String obj, String errmsg, Object... args) {
        if (StringUtils.hasText(obj)) {
            throw new ServiceException(errmsg, args);
        }
    }
}
