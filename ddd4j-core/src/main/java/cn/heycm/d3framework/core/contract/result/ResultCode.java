package cn.heycm.d3framework.core.contract.result;

import cn.heycm.d3framework.core.contract.enums.IEnum;

/**
 * 响应码
 * @author heycm
 * @version 1.0
 * @since 2025/8/2 18:04
 */
public enum ResultCode implements IEnum<Integer> {

    SUCCESS(200, "成功"),
    ERROR(500, "服务异常"),

    // ---- 系统级错误码：400-1999 ---------------------------------------------
    INVALID_PARAM(400, "参数错误"),
    ERROR_AUTHENTICATION(401, "认证失败"),
    ERROR_AUTHORIZATION(403, "权限不足"),
    RESOURCE_NOT_FOUND(404, "资源不存在"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    SERVICE_FALLBACK(503, "服务降级"),
    SERVICE_TIMEOUT(504, "服务超时"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    ERROR_BLOCKED_SENTINEL(1071, "Blocked by Sentinel"),
    ERROR_BLOCKED_FLOW(1072, "服务限流"),
    ERROR_BLOCKED_DEGRADE(1073, "服务降级"),
    ERROR_BLOCKED_PARAM(1074, "热点参数限流"),
    ERROR_BLOCKED_SYSTEM(1075, "系统异常"),
    ERROR_BLOCKED_AUTHORITY(1076, "授权异常"),
    TOO_MANY_REQUEST(1080, "服务拥挤，请稍后再试"),

    // ---- 通用业务错误码：2000-2999 ---------------------------------------------
    PARAM_MISSING(2001, "参数缺失"),
    NO_DATA(2002, "不存在相应的数据信息"),
    SAVE_FAILED(2003, "相应的数据信息保存失败"),
    DEL_FAILED(2004, "相应的数据信息删除失败"),
    MEDIA_NOT_SUPPORT(2005, "不支持媒体文件类型"),
    UPLOAD_FAILED(2006, "文件上传失败"),
    LOCK_FAILED(2007, "锁定资源失败"),
    COMMAND_TIMEOUT(2008, "命令执行超时"),
    QUERY_TIMEOUT(2009, "查询执行超时");

    /**
     * 响应码值
     */
    private final Integer code;

    /**
     * 响应码描述
     */
    private final String desc;

    ResultCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String message() {
        return desc;
    }
}
