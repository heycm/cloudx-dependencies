package com.github.heycm.cloudx.core.contract.exception;

import com.github.heycm.cloudx.core.contract.result.IR;
import com.github.heycm.cloudx.core.contract.result.ResultCode;
import com.github.heycm.cloudx.core.utils.StrKit;
import java.io.Serializable;
import lombok.Getter;

/**
 * 业务业务异常
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 18:15
 */
@Getter
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

    public ServiceException(ResultCode resultCode) {
        super(resultCode.message());
        this.code = resultCode.code();
    }

    public ServiceException(IR<?> r) {
        super(r.getMessage());
        this.code = r.getCode();
    }

    public ServiceException(String template, Object... args) {
        super(StrKit.format(template, args));
        this.code = ResultCode.ERROR.code();
    }
}
