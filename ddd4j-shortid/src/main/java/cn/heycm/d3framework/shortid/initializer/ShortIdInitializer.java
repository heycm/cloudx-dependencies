package cn.heycm.d3framework.shortid.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 初始化
 * @author heycm
 * @version 1.0
 * @since 2025/8/30 23:37
 */
public class ShortIdInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortIdInitializer.class);

    private final JdbcTemplate jdbcTemplate;

    public ShortIdInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void initialize() {
        String findTable = "SHOW TABLES LIKE 'ddd4j_short_id'";
        Integer i = jdbcTemplate.queryForObject(findTable, Integer.class);
        if (i != null) {
            LOGGER.info("Table 'ddd4j_short_id' already exists.");
            return;
        }
        String createTable = """
                CREATE TABLE `ddd4j_short_id` (
                  `id_key` varchar(50) NOT NULL COMMENT '短ID主键',
                  `id_value` int(11) NOT NULL COMMENT '当前最大ID值',
                  `id_step` int(11) NOT NULL COMMENT 'ID号段步长',
                  `create_time` datetime DEFAULT current_timestamp() COMMENT '创建时间',
                  `update_time` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
                  PRIMARY KEY (`id_key`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='DDD4J-短ID生成表'
                """;
        jdbcTemplate.update(createTable);
        LOGGER.info("Table 'ddd4j_short_id' created successfully.");
    }
}
