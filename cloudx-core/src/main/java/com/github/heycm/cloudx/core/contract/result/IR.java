package com.github.heycm.cloudx.core.contract.result;

import com.github.heycm.cloudx.core.utils.Assert;
import java.io.Serializable;

/**
 * 标准请求响应数据结构
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 22:41
 */
public interface IR<T> extends Serializable {

    /**
     * 响应码
     * @return 响应码
     */
    Serializable getCode();

    /**
     * 响应信息
     * @return 响应信息
     */
    String getMessage();

    /**
     * 响应数据
     * @return 数据
     */
    T getData();

    /**
     * 响应是否成功
     * @return true/false
     */
    boolean isOk();

    /**
     * 断言响应成功
     */
    default void assertOk() {
        Assert.isOk(this);
    }

    /**
     * 断言响应成功且数据不为空
     * @return 数据
     */
    default T assertData() {
        this.assertOk();
        T data = this.getData();
        Assert.notNull(data, "result data is null");
        return data;
    }
}
