package com.github.heycm.cloudx.core.domain.tracking;

/**
 * 状态指纹计算字段提供者，为计算状态指纹提供必要的字段
 * @author heycm
 * @version 1.0
 * @since 2025/11/15 14:24
 */
public interface FingerprintFieldProvider {

    /**
     * 获取参与状态指纹计算的相关字段
     * @return 字段数组
     */
    Object[] getFingerprintFields();
}
