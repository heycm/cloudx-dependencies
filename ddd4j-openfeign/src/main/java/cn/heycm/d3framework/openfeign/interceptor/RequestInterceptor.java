package cn.heycm.d3framework.openfeign.interceptor;

import cn.heycm.d3framework.core.contract.constant.AppConstant;
import cn.heycm.d3framework.openfeign.matcher.TransferHeaderMatcher;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Optional;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 请求拦截，设置请求头
 * @author heycm
 * @version 1.0
 * @since 2025/3/24 23:09
 */
public class RequestInterceptor implements feign.RequestInterceptor {

    @Autowired(required = false)
    private TransferHeaderMatcher transferHeaderMatcher;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // Web 请求头
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    if (needTransmit(name)) {
                        String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                }
            }
        }

        // 链路追踪
        setTraceId(requestTemplate);
    }

    private boolean needTransmit(String headerName) {
        return AppConstant.TRACE_ID.equalsIgnoreCase(headerName)
                || AppConstant.TOKEN_HEADER.equalsIgnoreCase(headerName)
                || AppConstant.TERMINAL.equalsIgnoreCase(headerName)
                || AppConstant.UID.equalsIgnoreCase(headerName)
                || Optional.ofNullable(transferHeaderMatcher)
                .map(matcher -> matcher.needTransmit(headerName)).orElse(false);
    }

    private void setTraceId(RequestTemplate requestTemplate) {
        String traceId = MDC.get(AppConstant.TRACE_ID);
        if (traceId != null) {
            requestTemplate.header(AppConstant.TRACE_ID, traceId);
        }
        String uid = MDC.get(AppConstant.UID);
        if (uid != null) {
            requestTemplate.header(AppConstant.UID, uid);
        }
    }
}
