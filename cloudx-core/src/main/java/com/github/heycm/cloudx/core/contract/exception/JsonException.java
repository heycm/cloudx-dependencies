package com.github.heycm.cloudx.core.contract.exception;

/**
 * Json操作异常
 * @author heycm
 * @version 1.0
 * @since 2025/8/28 21:52
 */
public class JsonException extends RuntimeException {

    public JsonException(Throwable t) {
        super(t);
    }
}
