package cn.heycm.d3framework.core.contract.result;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 分页模型
 * @author heycm
 * @version 1.0
 * @since 2025/8/5 22:45
 */
@Data
public class Page<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2982914859601070883L;

    /**
     * 当前页码
     */
    private int page;

    /**
     * 每页数量
     */
    private int size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 数据列表
     */
    private List<E> list;
}
