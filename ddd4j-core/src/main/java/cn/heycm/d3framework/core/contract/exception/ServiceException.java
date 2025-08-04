package cn.heycm.d3framework.core.contract.exception;

import cn.heycm.d3framework.core.contract.enums.IEnum;
import cn.heycm.d3framework.core.contract.result.IR;
import cn.heycm.d3framework.core.contract.result.ResultCode;
import java.io.Serializable;

/**
 * 业务业务异常
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 18:15
 */
public class ServiceException extends RuntimeException {

    private final Serializable code;

    public ServiceException(String message) {
        super(message);
        this.code = ResultCode.ERROR.code();
    }

    public ServiceException(Serializable code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(IEnum<Serializable> resultCode) {
        super(resultCode.message());
        this.code = resultCode.code();
    }

    public ServiceException(IR r) {
        super(r.getMessage());
        this.code = r.getCode();
    }
}
