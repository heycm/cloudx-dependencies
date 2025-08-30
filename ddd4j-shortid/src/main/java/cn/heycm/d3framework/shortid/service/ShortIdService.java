package cn.heycm.d3framework.shortid.service;

import cn.heycm.d3framework.shortid.entity.ShortId;
import cn.heycm.d3framework.shortid.exception.ShortIdException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 短ID生成服务
 * @author heycm
 * @version 1.0
 * @since 2025/8/30 23:01
 */
public class ShortIdService {

    private final JdbcTemplate jdbcTemplate;

    public ShortIdService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 获取短ID
     * @param idKey 短ID标识
     * @return 短ID
     */
    public ShortId acquireId(String idKey) {
        return acquireId(idKey, 3);
    }

    /**
     * 获取短ID
     * @param idKey    短ID标识
     * @param maxRetry 最大重试次数
     * @return 短ID
     */
    public ShortId acquireId(String idKey, int maxRetry) {
        maxRetry = Math.max(maxRetry, 1);
        while (maxRetry-- > 0) {
            ShortIdPO shortIdPO = findIdKey(idKey);
            if (shortIdPO == null) {
                throw new ShortIdException("Failed to acquire next ShortId: IdKey Not Found.");
            }
            int row = incrShortId(shortIdPO);
            if (row > 0) {
                ShortId shortId = new ShortId();
                shortId.setIdKey(shortIdPO.getIdKey());
                shortId.setNextId(shortIdPO.getIdValue() + 1);
                shortId.setMaxId(shortIdPO.getIdValue() + shortIdPO.getIdStep());
                return shortId;
            }
        }
        throw new ShortIdException("Failed to acquire next ShortId.");
    }

    private int incrShortId(ShortIdPO shortIdPO) {
        String sql = "UPDATE ddd4j_short_id SET id_value = id_value + id_step WHERE id_key = ? AND id_value = ?";
        return jdbcTemplate.update(sql, shortIdPO.getIdKey(), shortIdPO.getIdValue());
    }

    private ShortIdPO findIdKey(String idKey) {
        String sql = "SELECT id_key, id_value, id_step FROM ddd4j_short_id WHERE id_key = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                ShortIdPO shortId = new ShortIdPO();
                shortId.setIdKey(rs.getString("id_key"));
                shortId.setIdValue(rs.getInt("id_value"));
                shortId.setIdStep(rs.getInt("id_step"));
                return shortId;
            }, idKey);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
