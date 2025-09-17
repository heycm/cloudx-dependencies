package com.github.heycm.cloudx.mybatis;

import com.github.heycm.cloudx.core.context.SpringContext;
import com.github.heycm.cloudx.mybatis.plugins.SQLMarkingInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/25 22:26
 */
@Configuration
public class MybatisAutoConfiguration implements SmartInitializingSingleton {

    @Bean
    public SQLMarkingInterceptor sqlMarkingInterceptor() {
        return new SQLMarkingInterceptor();
    }

    /**
     * Spring容器中，所有单例Bean创建完毕之后的回调，确保 SqlSessionFactory 已经创建
     */
    @Override
    public void afterSingletonsInstantiated() {
        SqlSessionFactory bean = SpringContext.getBean(SqlSessionFactory.class);
        BatchHelper.setSqlSessionFactory(bean);
    }
}
