package com.github.heycm.cloudx.ds.tenant;

import com.github.heycm.cloudx.core.context.TenantContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多租户数据源
 * @author heycm
 * @version 1.0
 * @since 2025/8/10 14:34
 */
public class TenantDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenantId();
    }
}
