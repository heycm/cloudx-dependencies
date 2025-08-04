package cn.heycm.d3framework.core.contract.result;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;

/**
 * 统一接口响应
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 23:01
 */
@Getter
public class Result<T> implements IR<T> {

    @Serial
    private static final long serialVersionUID = -2723785958306510683L;

    public static final Result<Void> OK = Result.ok();

    public static final Result<Void> FAIL = Result.fail();

    /**
     * 响应码
     */
    private Serializable code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    @Override
    public Serializable getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public boolean isOk() {
        return ResultCode.SUCCESS.code().equals(code);
    }

    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.code = ResultCode.SUCCESS.code();
        result.message = ResultCode.SUCCESS.message();
        return result;
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = Result.ok();
        result.data = data;
        return result;
    }

    public static <T> Result<T> ok(T data, String message) {
        Result<T> result = Result.ok(data);
        result.message = message;
        return result;
    }

    public static <T> Result<T> fail() {
        Result<T> result = new Result<>();
        result.code = ResultCode.ERROR.code();
        result.message = ResultCode.ERROR.message();
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = Result.fail();
        result.message = message;
        return result;
    }

    public static <T> Result<T> fail(Serializable code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        return result;
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return Result.fail(resultCode.code(), resultCode.message());
    }
}
