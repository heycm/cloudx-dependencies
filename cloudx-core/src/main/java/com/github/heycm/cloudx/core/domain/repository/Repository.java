package com.github.heycm.cloudx.core.domain.repository;

import com.github.heycm.cloudx.core.context.SpringContext;
import com.github.heycm.cloudx.core.domain.model.Entity;
import com.github.heycm.cloudx.core.domain.model.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储层
 * @author heycm
 * @version 1.0
 * @since 2025/8/9 18:27
 */
public interface Repository<M extends Entity<ID>, ID extends Identifier> {

    Map<Class<?>, Class<?>> REPOSITORY_MAPPINGS = new ConcurrentHashMap<>();

    /**
     * 注入 Model 和 Repository 的映射关系
     * @param modelClass      领域模型类
     * @param repositoryClass 存储层类
     * @param <M>             模型类型
     * @param <ID>            模型标识符类型
     * @param <R>             存储层类型
     */
    static <M extends Entity<ID>, ID extends Identifier, R extends Repository<M, ID>> void inject(Class<M> modelClass,
            Class<R> repositoryClass) {
        REPOSITORY_MAPPINGS.put(modelClass, repositoryClass);
    }

    /**
     * 获取领域模型的存储层 Bean 实例
     * @param modelClass 领域模型类
     * @param <M>        模型类型
     * @param <ID>       模型标识符类型
     * @param <R>        存储层类型
     * @return 存储层 Bean 实例
     */
    @SuppressWarnings("unchecked")
    static <M extends Entity<ID>, ID extends Identifier, R extends Repository<M, ID>> R of(Class<M> modelClass) {
        return SpringContext.getBean((Class<R>) REPOSITORY_MAPPINGS.get(modelClass));
    }
}
