package com.github.heycm.cloudx.openfeign.matcher;

/**
 * 传递请求头匹配器
 * @author heycm
 * @version 1.0
 * @since 2025/8/27 23:36
 */
public interface TransferHeaderMatcher {

    /**
     * 是否需要传递请求头
     * @param headerName 请求头名称
     * @return true: 需要传递
     */
    boolean needTransmit(String headerName);
}
