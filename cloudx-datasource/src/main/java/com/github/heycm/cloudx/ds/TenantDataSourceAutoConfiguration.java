package com.github.heycm.cloudx.ds;

import com.github.heycm.cloudx.core.contract.datasource.DataSourceItem;
import com.github.heycm.cloudx.core.utils.Assert;
import com.github.heycm.cloudx.core.utils.Jackson;
import com.github.heycm.cloudx.ds.aspect.TenantAspect;
import com.github.heycm.cloudx.ds.utils.DataSourceUtil;
import com.github.heycm.cloudx.nacos.service.NacosConfListener;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 多租户数据源配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/23 23:26
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnClass({DispatcherServlet.class, NacosConfListener.class})
@ConditionalOnProperty(name = "cloudx.datasource.tenant", havingValue = "true")
public class TenantDataSourceAutoConfiguration {

    private static final String GROUP = "DATASOURCE";
    private static final String DATA_ID = "datasource.json";

    private List<DataSourceItem> tenantDataSourceItems;

    public TenantDataSourceAutoConfiguration(NacosConfListener nacosConfListener, DataSourceItem dataSourceItem) {
        Assert.notBlank(dataSourceItem.getSchema(), "Properties of cloudx.datasource.schema is blank.");
        nacosConfListener.addListener(DATA_ID, GROUP, conf -> {
            List<DataSourceItem> list = Jackson.toList(conf, DataSourceItem.class);
            list = list == null ? Collections.emptyList() : list;
            tenantDataSourceItems = list.stream().filter(item -> dataSourceItem.getSchema().equals(item.getSchema())).toList();
            Assert.isTrue(!tenantDataSourceItems.isEmpty(), "Can not find datasource config by schema: {}", dataSourceItem.getSchema());
        });
    }

    /**
     * 注册多租户数据源
     * @return dataSource
     */
    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        return DataSourceUtil.createTenantDataSource(tenantDataSourceItems);
    }

    @Bean
    public TenantAspect tenantAspect() {
        return new TenantAspect();
    }

}
