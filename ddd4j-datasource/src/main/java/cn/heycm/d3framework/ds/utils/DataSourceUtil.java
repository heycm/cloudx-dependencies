package cn.heycm.d3framework.ds.utils;

import cn.heycm.d3framework.core.contract.datasource.DataSourceItem;
import cn.heycm.d3framework.core.contract.datasource.DataSourceItemPool;
import cn.heycm.d3framework.ds.tenant.TenantDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * 数据源工具
 * @author heycm
 * @version 1.0
 * @since 2025/8/10 14:26
 */
public class DataSourceUtil {

    /**
     * 创建数据源
     * @param item 数据源配置项
     * @return 数据源
     * @throws ClassNotFoundException
     */
    public static HikariDataSource createDataSource(DataSourceItem item) throws ClassNotFoundException {
        Class.forName(item.getDriverClassName());
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(item.getDriverClassName());
        dataSource.setJdbcUrl(item.getJdbcUrl());
        dataSource.setUsername(item.getUsername());
        dataSource.setPassword(item.getPassword());
        DataSourceItemPool pool = item.getItemPool();
        if (null != pool) {
            dataSource.setPoolName(pool.getPoolName());
            dataSource.setMaximumPoolSize(pool.getMaxPoolSize());
            dataSource.setMinimumIdle(pool.getMinIdle());
            dataSource.setIdleTimeout(pool.getIdleTimeout());
            dataSource.setMaxLifetime(pool.getMaxLifetime());
            dataSource.setKeepaliveTime(pool.getKeepaliveTime());
            dataSource.setConnectionTimeout(pool.getConnectionTimeout());
            dataSource.setValidationTimeout(pool.getValidationTimeout());
            dataSource.setAutoCommit(pool.isAutoCommit());
            if (StringUtils.hasText(pool.getConnectionTestQuery())) {
                dataSource.setConnectionTestQuery(pool.getConnectionTestQuery());
            }
        }
        return dataSource;
    }

    /**
     * 创建多租户数据源
     * @param items 数据源项
     * @return 多租户数据源
     */
    public static TenantDataSource createTenantDataSource(List<DataSourceItem> items) throws ClassNotFoundException {
        TenantDataSource tenantAll = new TenantDataSource();
        Map<Object, Object> datasources = new HashMap<>();
        for (DataSourceItem item : items) {
            HikariDataSource ds = DataSourceUtil.createDataSource(item);
            datasources.put(item.getTenantId(), ds);
            if (item.isPrimary()) {
                tenantAll.setDefaultTargetDataSource(ds);
            }
        }
        tenantAll.setTargetDataSources(datasources);
        return tenantAll;
    }
}
