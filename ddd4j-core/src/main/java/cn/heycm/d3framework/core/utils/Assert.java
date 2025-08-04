package cn.heycm.d3framework.core.utils;

import cn.heycm.d3framework.core.contract.exception.ServiceException;
import cn.heycm.d3framework.core.contract.result.IR;
import cn.heycm.d3framework.core.contract.result.ResultCode;

/**
 * 断言工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 22:45
 */
public class Assert {

    private Assert() {}

    public static <T> void isOk(IR<T> r) {
        if (r == null) {
            throw new ServiceException(ResultCode.ERROR.code(), "result is null");
        }
        if (!r.isOk()) {
            throw new ServiceException(r);
        }
    }

    public static <T> void notNull(T obj, String message) {
        if (obj == null) {
            throw new ServiceException(ResultCode.ERROR.code(), message);
        }
    }
}
