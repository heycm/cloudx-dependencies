package com.github.heycm.cloudx.web.session;

import com.github.heycm.cloudx.core.contract.constant.AppConstant;
import com.github.heycm.cloudx.core.contract.result.ResultCode;
import com.github.heycm.cloudx.core.utils.Assert;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Session 解析器，在控制层方法参数中自动获取当前请求的 Session 对象，用法类似 HttpServletRequest
 * @author heycm
 * @version 1.0
 * @since 2024-11-16 13:56
 */
public abstract class AbstractSessionResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Session.class == parameter.getParameterType();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        // 获取请求头中的 token
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.notNull(request, "request context missing");
        String token = request.getHeader(AppConstant.TOKEN_HEADER);
        Assert.notBlank(token, ResultCode.ERROR_AUTHENTICATION);
        Assert.isTrue(token.startsWith(AppConstant.TOKEN_PREFIX), ResultCode.ERROR_AUTHENTICATION);
        // Token -> Session
        return getSession(token.substring(AppConstant.TOKEN_PREFIX.length()));
    }

    /**
     * 获取Session
     * @param token
     * @return Session
     */
    protected abstract Session getSession(String token);
}
