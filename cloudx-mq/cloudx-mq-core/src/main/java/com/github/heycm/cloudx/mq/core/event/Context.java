package com.github.heycm.cloudx.mq.core.event;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 事件上下文
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 20:34
 */
@Data
public class Context implements Serializable {

    @Serial
    private static final long serialVersionUID = -7087631570652458863L;

    /**
     * 数据
     */
    private String data;


    public static Context of(String data) {
        Context context = new Context();
        context.data = data;
        return context;
    }
}
