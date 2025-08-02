package cn.heycm.d3framework.core.contract.exception;

import cn.heycm.d3framework.core.contract.enums.IEnum;
import cn.heycm.d3framework.core.contract.result.ResultCode;

/**
 * 业务业务异常
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 18:15
 */
public class ServiceException extends RuntimeException {

    private final Integer code;

    public ServiceException(String message) {
        super(message);
        this.code = ResultCode.ERROR.code();
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(IEnum<Integer> resultCode) {
        super(resultCode.message());
        this.code = resultCode.code();
    }
}
