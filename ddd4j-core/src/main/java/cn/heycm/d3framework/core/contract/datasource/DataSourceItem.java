package cn.heycm.d3framework.core.contract.datasource;

import java.text.MessageFormat;
import lombok.Data;

/**
 * 数据源配置项
 * @author heycm
 * @version 1.0
 * @since 2025/3/22 19:00
 */
@Data
public class DataSourceItem {

    public static final MessageFormat JDBC_FORMAT = new MessageFormat("jdbc:mysql://{0}:{1}/{2}"
            + "?useUnicode=true" // 使用Unicode编码
            + "&characterEncoding=utf-8" // 设置字符集
            + "&useSSL=false" // 关闭SSL
            + "&serverTimezone=Asia/Shanghai" // 设置时区
            + "&allowPublicKeyRetrieval=true" // 允许公钥检索
            + "&rewriteBatchedStatements=true"  // 批量处理
            + "&useServerPrepStmts=true" // 启用服务端编译提高性能但是会导致时间类型的转换可能出问题，需要测试
            + "&cachePrepStmts=true" // 缓存预编译语句
            + "&prepStmtCacheSize=250" // 缓存预编译语句大小
            + "&prepStmtCacheSqlLimit=2048" // 缓存预编译语句最大长度
            + "&tcpKeepAlive=true" // TCP保活，JDBC4以上才支持，需要清空连接池中connectionTestQuery
            + "&maintainTimeStats=false" // 不保存时间统计
    );

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 是否主（默认）数据源
     */
    private boolean primary = false;

    /**
     * 驱动类
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库连接地址
     */
    private String host;

    /**
     * 数据库端口
     */
    private int port = 3306;

    /**
     * 数据库名称
     */
    private String schema;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 连接池配置
     */
    private DataSourceItemPool itemPool;

    /**
     * 获取jdbc连接地址
     * @return JDBC URL
     */
    public String getJdbcUrl() {
        return JDBC_FORMAT.format(new Object[]{host, port + "", schema});
    }
}
