package com.github.heycm.cloudx.core.domain.cqrs.command;

import com.github.heycm.cloudx.core.contract.result.ResultCode;
import com.github.heycm.cloudx.core.utils.Assert;
import com.github.heycm.cloudx.core.utils.Optional;
import com.github.heycm.cloudx.core.utils.ReflectionUtil;
import com.github.heycm.cloudx.core.utils.threadpool.virtual.VirtualThread;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.context.ApplicationContext;

/**
 * APP命令总线实现
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 21:40
 */
public class AppCommandBusImpl implements CommandBus {

    private static final int initialCapacity = 256;

    private final ApplicationContext applicationContext;

    private final Map<Class<?>, CommandHandler<?, ?>> commandHandlers = new ConcurrentHashMap<>(initialCapacity);

    public AppCommandBusImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.registerHandlers();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, T extends Command<R>> Optional<R> dispatch(T command) {
        Assert.notNull(command, "Command cannot be null");
        CommandHandler<T, R> handler = (CommandHandler<T, R>) commandHandlers.get(command.getClass());
        Assert.notNull(handler, "Command handler not found: {}", command.getClass().getName());
        return handler.handle(command);
    }

    @Override
    public <R, T extends Command<R>> CompletableFuture<Optional<R>> dispatchAsync(T command) {
        return CompletableFuture.supplyAsync(() -> dispatch(command), VirtualThread.EXECUTOR);
    }

    @Override
    public <R, T extends Command<R>> CompletableFuture<Optional<R>> dispatchAsync(T command, Duration timeout) {
        CompletableFuture<Optional<R>> future = dispatchAsync(command);
        if (timeout != null) {
            return future.completeOnTimeout(Optional.fail(ResultCode.COMMAND_TIMEOUT), timeout.toMillis(), TimeUnit.MILLISECONDS);
        }
        return future;
    }

    private void registerHandlers() {
        Map<String, CommandHandler> beans = applicationContext.getBeansOfType(CommandHandler.class);
        for (CommandHandler handler : beans.values()) {
            Class<?> commandClass = ReflectionUtil.getObjectSuperclassGenericType(handler, CommandHandler.class, 0);
            commandHandlers.putIfAbsent(commandClass, handler);
        }
    }
}
