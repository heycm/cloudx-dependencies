package cn.heycm.d3framework.sentinel;

import cn.heycm.d3framework.core.contract.result.Result;
import cn.heycm.d3framework.core.contract.result.ResultCode;
import cn.heycm.d3framework.core.utils.IOUtil;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.WebFluxCallbackManager;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Sentinel配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/30 22:29
 */
@Configuration
@ConditionalOnProperty(value = "spring.cloud.sentinel.transport.dashboard")
public class SentinelAutoConfiguration {

    /**
     * WebMVC环境下启动
     */
    @Bean
    @ConditionalOnClass(DispatcherServlet.class)
    public BlockExceptionHandler blockExceptionHandler() {
        return (httpServletRequest, httpServletResponse, e) -> {
            Result<Void> result = convertResult(e);
            IOUtil.writeJson(httpServletResponse, result);
        };
    }

    /**
     * WebFlux环境下启动
     */
    @Bean
    @ConditionalOnClass(DispatcherHandler.class)
    public BlockRequestHandler blockRequestHandler() {
        BlockRequestHandler handler = (serverWebExchange, throwable) -> {
            Result<Void> result = convertResult(throwable);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result);
        };
        WebFluxCallbackManager.setBlockHandler(handler);
        return handler;
    }

    private static Result<Void> convertResult(Throwable throwable) {
        return switch (throwable) {
            case FlowException ignored -> Result.fail(ResultCode.ERROR_BLOCKED_FLOW);
            case DegradeException ignored -> Result.fail(ResultCode.ERROR_BLOCKED_DEGRADE);
            case ParamFlowException ignored -> Result.fail(ResultCode.ERROR_BLOCKED_PARAM);
            case SystemBlockException ignored -> Result.fail(ResultCode.ERROR_BLOCKED_SYSTEM);
            case AuthorityException ignored -> Result.fail(ResultCode.ERROR_BLOCKED_AUTHORITY);
            case null, default -> Result.fail(ResultCode.ERROR_BLOCKED_SENTINEL);
        };
    }
}
