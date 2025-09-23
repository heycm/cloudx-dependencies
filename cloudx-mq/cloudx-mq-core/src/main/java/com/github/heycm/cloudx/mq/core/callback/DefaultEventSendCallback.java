package com.github.heycm.cloudx.mq.core.callback;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认异步事件发送回调
 * @author heycm
 * @version 1.0
 * @since 2025/9/23 22:19
 */
@Slf4j
public class DefaultEventSendCallback implements EventSendCallback {

    @Override
    public <T> void onSuccess(T sendResult) {
        if (log.isDebugEnabled()) {
            log.debug("Send event success, sendResult: {}", sendResult);
        }
    }

    @Override
    public void onException(Throwable e) {
        log.error("Send event error: {}", e.getMessage(), e);
    }
}
