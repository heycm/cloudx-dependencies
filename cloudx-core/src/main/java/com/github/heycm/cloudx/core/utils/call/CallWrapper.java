package com.github.heycm.cloudx.core.utils.call;

import com.github.heycm.cloudx.core.utils.Jackson;
import java.util.Optional;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * 方法调用包装
 * @author heycm
 * @version 1.0
 * @since 2025/8/9 19:56
 */
@Slf4j
public class CallWrapper {

    private CallWrapper() {
    }

    /**
     * 包装方法调用，自动输出出入参、耗时、异常堆栈信息日志
     * @param callPoint 调用点
     * @param function  方法
     * @param arg       入参
     * @param <T>       入参类型
     * @param <R>       出参类型
     * @return 返回值
     */
    public static <T, R> R call(String callPoint, Function<T, R> function, T arg) {
        boolean logInfoEnabled = log.isInfoEnabled();
        StopWatch sw = logInfoEnabled ? new StopWatch() : null;
        R result = null;
        try {
            Optional.ofNullable(sw).ifPresent(StopWatch::start);
            result = function.apply(arg);
        } catch (Throwable e) {
            log.error("调用点 {} 发生异常: {}", callPoint, e.getMessage(), e);
            throw e;
        } finally {
            Optional.ofNullable(sw).ifPresent(StopWatch::stop);
            if (logInfoEnabled) {
                long cost = sw.getTotalTimeMillis();
                log.info("调用点 {} 耗时: {}ms 参数: {} 结果: {}", callPoint, cost, Jackson.toJson(arg), Jackson.toJson(result));
            }
        }
        return result;
    }

    /**
     * 包装方法调用，自动输出出入参、耗时、异常堆栈信息日志
     * @param callPoint 调用点
     * @param function  方法
     * @param <T>       入参类型
     * @param <R>       出参类型
     * @return 返回值
     */
    public static <T, R> R call(String callPoint, Function<T, R> function) {
        return CallWrapper.call(callPoint, function, null);
    }
}
