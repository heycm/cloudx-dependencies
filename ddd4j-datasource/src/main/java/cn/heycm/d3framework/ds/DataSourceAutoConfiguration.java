package cn.heycm.d3framework.ds;

import cn.heycm.d3framework.ds.transaction.TransactionHelper;
import javax.sql.DataSource;
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


    public DataSourceAutoConfiguration() {

    }

    @Bean
    public DataSource dataSource() {
        return null;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionHelper transactionHelper(PlatformTransactionManager platformTransactionManager) {
        return new TransactionHelper(platformTransactionManager);
    }
}
