package com.github.heycm.cloudx.openfeign.util;

import com.github.heycm.cloudx.core.contract.result.Result;
import com.github.heycm.cloudx.core.contract.result.ResultCode;
import feign.FeignException.ServiceUnavailable;
import feign.RetryableException;

/**
 * Feign工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/27 23:20
 */
public class FeignUtil {

    /**
     * Feign调用异常解析，转换错误码
     * @param throwable 错误
     * @return 响应
     */
    public static <T> Result<T> explain(Throwable throwable) {
        // 服务超时
        if (throwable instanceof RetryableException) {
            return Result.fail(ResultCode.SERVICE_TIMEOUT);
        }
        // 服务不可用
        if (throwable instanceof ServiceUnavailable) {
            return Result.fail(ResultCode.SERVICE_UNAVAILABLE);
        }
        return Result.fail(ResultCode.SERVICE_FALLBACK);
    }

}
