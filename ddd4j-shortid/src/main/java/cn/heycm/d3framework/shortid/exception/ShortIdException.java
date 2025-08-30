package cn.heycm.d3framework.shortid.exception;

/**
 * 获取短ID异常
 * @author heycm
 * @version 1.0
 * @since 2025/8/30 23:07
 */
public class ShortIdException extends RuntimeException {

    public ShortIdException(String message) {
        super(message);
    }

    public ShortIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
