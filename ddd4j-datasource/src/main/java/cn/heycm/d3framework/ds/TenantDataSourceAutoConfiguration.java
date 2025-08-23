package cn.heycm.d3framework.ds;

import cn.heycm.d3framework.core.contract.datasource.DataSourceItem;
import cn.heycm.d3framework.core.utils.Assert;
import cn.heycm.d3framework.core.utils.Jackson;
import cn.heycm.d3framework.ds.aspect.TenantAspect;
import cn.heycm.d3framework.ds.utils.DataSourceUtil;
import cn.heycm.d3framework.nacos.service.NacosConfListener;
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
@ConditionalOnProperty(name = "ddd4j.datasource.tenant", havingValue = "true")
public class TenantDataSourceAutoConfiguration {

    private static final String GROUP = "DATASOURCE";
    private static final String DATA_ID = "datasource.json";

    private List<DataSourceItem> tenantDataSourceItems;

    public TenantDataSourceAutoConfiguration(NacosConfListener nacosConfListener, DataSourceItem dataSourceItem) {
        Assert.notBlank(dataSourceItem.getSchema(), "Properties of ddd4j.datasource.schema is blank.");
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
