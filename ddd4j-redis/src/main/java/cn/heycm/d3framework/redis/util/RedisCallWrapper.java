package cn.heycm.d3framework.redis.util;

import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisCallWrapper {

    public static <V> V call(Callable<V> supplier) {
        try {
            return supplier.call();
        } catch (Exception e) {
            log.error("Redis call error: {}", e.getMessage(), e);
        }
        return null;
    }

    public static boolean boolCall(Callable<Boolean> supplier) {
        Boolean call = RedisCallWrapper.call(supplier);
        return Boolean.TRUE.equals(call);
    }

    public static long longCall(Callable<Long> supplier) {
        Long call = RedisCallWrapper.call(supplier);
        return call == null ? 0 : call;
    }

}
