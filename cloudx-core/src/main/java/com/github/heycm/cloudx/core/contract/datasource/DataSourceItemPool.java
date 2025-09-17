package com.github.heycm.cloudx.core.contract.datasource;

import lombok.Data;

/**
 * 数据源连接池
 * @author heycm
 * @version 1.0
 * @since 2025-3-22 19:03
 */
@Data
public class DataSourceItemPool {

    /**
     * 连接池名称
     */
    private String poolName;

    /**
     * 连接池最大连接数，默认10
     */
    private int maxPoolSize = 10;

    /**
     * 最小连接数，默认跟最大连接数一致，根据官方建议设置与最大连接数相同，形成固定大小的连接池，以获得最佳性能
     */
    private int minIdle = 10;

    /**
     * 空闲连接超时时间（毫秒），超时将关闭连接，在 maxPoolSize > minIdle 时有效
     */
    private long idleTimeout = 600000;

    /**
     * 池中连接的最大生命周期，默认30分钟，正在使用的连接不会被淘汰，建议设置比数据库或中间设施的连接超时时间短几秒
     */
    private long maxLifetime = 21600000;

    /**
     * 空闲连接的保活频率，默认2分钟，调用JDBC4.isValid()方法或执行connectionTestQuery
     */
    private long keepaliveTime = 1800000;

    /**
     * 从连接池中获取连接的超时时间（毫秒）
     */
    private long connectionTimeout = 1000;

    /**
     * 验证连接有效性的超时时间,要求比获取连接超时时间少（毫秒）
     */
    private long validationTimeout = 500;

    /**
     * 池中连接有效性测试语句，如果驱动程序支持JDBC4不要设置此属性，使用JDBC4.isValid()方法，如果不支持可以使用 "select 1"
     */
    private String connectionTestQuery;

    /**
     * 池中连接自动提交事务，默认true
     */
    private boolean autoCommit = true;
}
