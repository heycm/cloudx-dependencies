package cn.heycm.d3framework.core.domain.cqrs.command;


import cn.heycm.d3framework.core.utils.Optional;

/**
 * 命令处理器
 * @author heycm
 * @version 1.0
 * @since 2025/7/3 21:09
 */
public interface CommandHandler<T extends Command<R>, R> {

    /**
     * 执行命令
     * @param command 命令
     * @return 命令处理结果
     */
    Optional<R> handle(T command);

}
