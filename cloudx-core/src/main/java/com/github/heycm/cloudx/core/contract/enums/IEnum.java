package com.github.heycm.cloudx.core.contract.enums;

import java.io.Serializable;

/**
 * 枚举接口
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 18:01
 */
public interface IEnum<T extends Serializable> {

    /**
     * 获取枚举值
     * @return 枚举值
     */
    T code();

    /**
     * 获取枚举描述
     * @return 枚举描述
     */
    String message();
}
