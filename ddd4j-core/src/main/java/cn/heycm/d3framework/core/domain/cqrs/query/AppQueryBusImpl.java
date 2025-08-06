package cn.heycm.d3framework.core.domain.cqrs.query;

import cn.heycm.d3framework.core.contract.result.ResultCode;
import cn.heycm.d3framework.core.utils.Assert;
import cn.heycm.d3framework.core.utils.Optional;
import cn.heycm.d3framework.core.utils.ReflectionUtil;
import cn.heycm.d3framework.core.utils.threadpool.virtual.VirtualThread;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.context.ApplicationContext;

/**
 * APP查询总线实现
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 23:17
 */
public class AppQueryBusImpl implements QueryBus {

    private static final int initialCapacity = 256;

    private final ApplicationContext applicationContext;

    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers = new ConcurrentHashMap<>(initialCapacity);

    public AppQueryBusImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.registerHandlers();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends Query<R>> Optional<R> dispatch(T query) {
        Assert.notNull(query, "Query cannot be null");
        QueryHandler<T, R> handler = (QueryHandler<T, R>) queryHandlers.get(query.getClass());
        Assert.notNull(handler, "Query handler not found: {}", query.getClass().getName());
        return handler.handle(query);
    }

    @Override
    public <R, T extends Query<R>> CompletableFuture<Optional<R>> dispatchAsync(T query) {
        return CompletableFuture.supplyAsync(() -> dispatch(query), VirtualThread.EXECUTOR);
    }

    @Override
    public <R, T extends Query<R>> CompletableFuture<Optional<R>> dispatchAsync(T query, Duration timeout) {
        CompletableFuture<Optional<R>> future = dispatchAsync(query);
        if (timeout != null) {
            return future.completeOnTimeout(Optional.fail(ResultCode.QUERY_TIMEOUT), timeout.toMillis(), TimeUnit.MILLISECONDS);
        }
        return future;
    }

    private void registerHandlers() {
        Map<String, QueryHandler> beans = applicationContext.getBeansOfType(QueryHandler.class);
        for (QueryHandler handler : beans.values()) {
            Class<?> queryType = ReflectionUtil.getObjectSuperclassGenericType(handler, QueryHandler.class, 0);
            queryHandlers.putIfAbsent(queryType, handler);
        }
    }
}
