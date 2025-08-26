package cn.heycm.d3framework.uid;

import cn.heycm.d3framework.uid.properties.UidProperties;
import cn.heycm.d3framework.uid.service.BaiduUidServiceImpl;
import cn.heycm.d3framework.uid.service.UidService;
import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UID自动配置
 * @author heycm
 * @version 1.0
 * @since 2025/8/26 22:10
 */
@Configuration
@EnableConfigurationProperties(UidProperties.class)
public class UidAutoConfiguration {

    @Bean
    public WorkerIdAssigner workerIdAssigner(UidProperties uidProperties) {
        return uidProperties::getWorkerId;
    }

    @Bean
    public UidGenerator uidGenerator(WorkerIdAssigner workerIdAssigner, UidProperties uidProperties) {
        CachedUidGenerator uidGenerator = new CachedUidGenerator();
        uidGenerator.setWorkerIdAssigner(workerIdAssigner);
        uidGenerator.setTimeBits(uidProperties.getTimeBits());
        uidGenerator.setWorkerBits(uidProperties.getWorkerBits());
        uidGenerator.setSeqBits(uidProperties.getSeqBits());
        return uidGenerator;
    }

    @Bean
    public UidService uidService(UidGenerator uidGenerator) {
        BaiduUidServiceImpl uidService = new BaiduUidServiceImpl(uidGenerator);
        cn.heycm.d3framework.uid.UidGenerator.setUidService(uidService);
        return uidService;
    }
}
