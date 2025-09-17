package com.github.heycm.cloudx.ds;

import com.github.heycm.cloudx.core.contract.datasource.DataSourceItem;
import com.github.heycm.cloudx.ds.transaction.TransactionHelper;
import com.github.heycm.cloudx.ds.utils.DataSourceUtil;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 数据源配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/10 14:42
 */
@Configuration
public class DataSourceAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "cloudx.datasource")
    public DataSourceItem dataSourceItem() {
        return new DataSourceItem();
    }

    @Bean
    @ConditionalOnProperty(name = "cloudx.datasource.host")
    public DataSource dataSource(DataSourceItem dataSourceItem) throws ClassNotFoundException {
        return DataSourceUtil.createDataSource(dataSourceItem);
    }

    @Bean
    @ConditionalOnBean(DataSource.class)
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    @ConditionalOnBean(PlatformTransactionManager.class)
    public TransactionHelper transactionHelper(PlatformTransactionManager platformTransactionManager) {
        return new TransactionHelper(platformTransactionManager);
    }
}
