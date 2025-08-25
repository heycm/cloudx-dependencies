package cn.heycm.d3framework.mybatis;

import cn.heycm.d3framework.mybatis.batch.BatchHelper;
import cn.heycm.d3framework.mybatis.plugins.SQLMarkingInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/25 22:26
 */
@Configuration
public class MybatisAutoConfiguration {

    @Bean
    public SQLMarkingInterceptor sqlMarkingInterceptor() {
        return new SQLMarkingInterceptor();
    }

    @Bean
    public BatchHelper batchHelper(SqlSessionFactory sqlSessionFactory) {
        return new BatchHelper(sqlSessionFactory);
    }
}
